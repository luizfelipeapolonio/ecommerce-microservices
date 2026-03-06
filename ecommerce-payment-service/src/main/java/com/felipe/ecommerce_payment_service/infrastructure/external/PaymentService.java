package com.felipe.ecommerce_payment_service.infrastructure.external;

import com.felipe.ecommerce_payment_service.core.application.usecases.CreatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.infrastructure.exceptions.InsufficientCustomerBalanceException;
import com.felipe.ecommerce_payment_service.infrastructure.exceptions.UnsuccessfulTransactionException;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.CustomerBalanceTransaction;
import com.stripe.model.PaymentMethod;
import com.stripe.model.StripeSearchResult;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerBalanceTransactionCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerSearchParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.PaymentMethodCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaymentService {

  @Value("${stripe.key}")
  private String stripeKey;
  private StripeClient stripeClient;
  private final CreatePaymentUseCase createPaymentUseCase;
  private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

  public PaymentService(CreatePaymentUseCase createPaymentUseCase) {
    this.createPaymentUseCase = createPaymentUseCase;
  }

  @PostConstruct
  private void init() {
    this.stripeClient = new StripeClient(this.stripeKey);
  }

  public String processPayment(PaymentTransactionCreateCommand paymentCommand) {
    Customer customer = findOrCreateCustomer(paymentCommand.getCustomer());

    if (isCustomerBalanceInsufficient(customer, paymentCommand.getOrderAmount())) {
      logger.error(
        "Customer insufficient balance -> email: {} - currentBalance: {} - orderAmount: {}",
        customer.getEmail(), customer.getBalance(), paymentCommand.getOrderAmount()
      );
      throw new InsufficientCustomerBalanceException(
        "Saldo do cliente de email '%s' insuficiente".formatted(customer.getEmail())
      );
    }

    SessionCreateParams params = SessionCreateParams.builder()
      .setMode(SessionCreateParams.Mode.PAYMENT)
      .setSuccessUrl("http://localhost:8087/success") // TODO: create static pages and redirect to 8080
      .setCancelUrl("http://localhost:8087/cancel")
      .setCustomer(customer.getId())
      .setClientReferenceId(paymentCommand.getOrderId().toString())
      .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
      .addPaymentMethodType(SessionCreateParams.PaymentMethodType.BOLETO)
      .setInvoiceCreation(SessionCreateParams.InvoiceCreation.builder()
        .setEnabled(true)
        .build())
      .addLineItem(
        SessionCreateParams.LineItem.builder()
          .setQuantity(paymentCommand.getProductQuantity())
          .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
            .setCurrency("brl")
            .setUnitAmount(formatBigDecimalStringToValidLongValue(paymentCommand.getOrderAmount()))
            .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
              .setName(paymentCommand.getProductName())
              .putMetadata("order_id", paymentCommand.getOrderId().toString())
              .build())
            .build())
          .build()
      )
      .build();

    try {
      Session session = this.stripeClient.v1().checkout().sessions().create(params);
      Payment createdPayment = this.createPaymentUseCase.execute(
        paymentCommand.getOrderId(),
        paymentCommand.getOrderAmount(),
        paymentCommand.getCustomer().id(),
        session.getId()
      );
      logger.info("Payment domain created successfully -> orderId: {}", createdPayment.getOrderId());

      CustomerBalanceTransactionCreateParams balanceParams = CustomerBalanceTransactionCreateParams.builder()
        .setCurrency("brl")
        .setAmount(session.getAmountTotal())
        .build();

      CustomerBalanceTransaction balanceTransaction = this.stripeClient.v1().customers().balanceTransactions().create(customer.getId(), balanceParams);
      logger.info("Customer balance executed -> id: {} - amount: {}", balanceTransaction.getCustomer(), balanceTransaction.getAmount());
      return session.getUrl();
    } catch (StripeException ex) {
      logger.error("Error in payment execution -> {}", ex.getStripeError().getMessage());
      throw new UnsuccessfulTransactionException("Stripe error on trying to execute payment", ex);
    }
  }

  public Customer createCustomer(PaymentTransactionCreateCommand.CustomerData customerData) {
    CustomerCreateParams params = CustomerCreateParams.builder()
      .setName(customerData.name())
      .setEmail(customerData.email())
      .setBalance(-10_000_00L) // R$ 10.000,00
      .setAddress(CustomerCreateParams.Address.builder()
        .setCity(customerData.address().city())
        .setState(customerData.address().state())
        .setPostalCode(customerData.address().zipcode())
        .setLine1(String.format(
          "%s, %s - %s-%s - complement: %s",
          customerData.address().street(),
          customerData.address().number(),
          customerData.address().district(),
          customerData.address().state(),
          customerData.address().complement()
        ))
        .setCountry(customerData.address().country())
        .build())
      .build();

    try {
      Customer customer = this.stripeClient.v1().customers().create(params);
      logger.info("Created Stripe customer -> id: {} - email: {}", customer.getId(), customer.getEmail());
      createPaymentMethod(customer);
      return customer;
    } catch (StripeException ex) {
      logger.error("Error in Stripe customer creation -> {}", ex.getStripeError().getMessage());
      throw new UnsuccessfulTransactionException("Stripe error on trying to create a customer", ex);
    }
  }

  private void createPaymentMethod(Customer customer) {
    PaymentMethodCreateParams cardParams = PaymentMethodCreateParams.builder()
      .setType(PaymentMethodCreateParams.Type.CARD)
      .setBillingDetails(PaymentMethodCreateParams.BillingDetails.builder()
        .setName(customer.getName())
        .setEmail(customer.getEmail())
        .setAddress(PaymentMethodCreateParams.BillingDetails.Address.builder()
          .setCity(customer.getAddress().getCity())
          .setState(customer.getAddress().getState())
          .setPostalCode(customer.getAddress().getPostalCode())
          .setLine1(customer.getAddress().getLine1())
          .setCountry(customer.getAddress().getCountry())
          .build())
        .build())
      .setCard(PaymentMethodCreateParams.Token.builder()
        .setToken("tok_visa")
        .build())
      .build();

    PaymentMethodCreateParams boletoParams = PaymentMethodCreateParams.builder()
      .setType(PaymentMethodCreateParams.Type.BOLETO)
      .setBillingDetails(PaymentMethodCreateParams.BillingDetails.builder()
        .setName(customer.getName())
        .setEmail(customer.getEmail())
        .setAddress(PaymentMethodCreateParams.BillingDetails.Address.builder()
          .setCity(customer.getAddress().getCity())
          .setState(customer.getAddress().getState())
          .setPostalCode(customer.getAddress().getPostalCode())
          .setLine1(customer.getAddress().getLine1())
          .setCountry(customer.getAddress().getCountry())
          .build())
        .build())
      .setBoleto(PaymentMethodCreateParams.Boleto.builder()
        .setTaxId("000.000.000-00")
        .build())
      .build();

    PaymentMethodAttachParams attachParams = PaymentMethodAttachParams.builder()
      .setCustomer(customer.getId())
      .build();

    try {
      PaymentMethod cardPaymentMethod = this.stripeClient.v1().paymentMethods().create(cardParams);
      logger.info("Card payment method created successfully");
      PaymentMethod boletoPaymentMethod = this.stripeClient.v1().paymentMethods().create(boletoParams);
      logger.info("Boleto payment method created successfully");

      this.stripeClient.v1().paymentMethods().attach(cardPaymentMethod.getId(), attachParams);
      this.stripeClient.v1().paymentMethods().attach(boletoPaymentMethod.getId(), attachParams);
    } catch (StripeException ex) {
      logger.error("Error in payment method creation -> {}", ex.getStripeError().getMessage());
      throw new UnsuccessfulTransactionException("Stripe error on trying to create payment methods", ex);
    }
  }

  private Long formatBigDecimalStringToValidLongValue(String value) {
    return Long.parseLong(value.replace(".", ""));
  }

  private Optional<Customer> findCustomerByEmail(String email) {
    CustomerSearchParams params = CustomerSearchParams.builder()
      .setQuery("email:'" + email + "'")
      .build();

    try {
      StripeSearchResult<Customer> result = this.stripeClient.v1().customers().search(params);
      logger.info("Find customer by email -> data: {}", result.getData().isEmpty() ? "EMPTY" : "FOUND");
      return result.getData().isEmpty() ? Optional.empty() : Optional.of(result.getData().getFirst());
    } catch (StripeException ex) {
      logger.error("Error in findCustomerByEmail -> {}", ex.getStripeError().getMessage());
      throw new UnsuccessfulTransactionException("Stripe error on trying to find a customer by email", ex);
    }
  }

  private Customer findOrCreateCustomer(PaymentTransactionCreateCommand.CustomerData customerData) {
    return findCustomerByEmail(customerData.email()).orElseGet(() -> createCustomer(customerData));
  }

  private boolean isCustomerBalanceInsufficient(Customer customer, String orderAmount) {
    long convertedOrderAmount = formatBigDecimalStringToValidLongValue(orderAmount);
    // It's necessary to use the negateExact because the customer.getBalance()
    // returns a negative value to represent a positive customer balance,
    // then we negate the balance value converting it from negative to positive
    // negateExact is safer because it throws an exception if negation overflows long range
    long customerBalance = Math.negateExact(customer.getBalance());
    return customerBalance < convertedOrderAmount;
  }
}
