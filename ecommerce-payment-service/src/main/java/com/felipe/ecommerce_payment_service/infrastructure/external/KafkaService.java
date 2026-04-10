package com.felipe.ecommerce_payment_service.infrastructure.external;

import com.felipe.ecommerce_payment_service.core.application.usecases.UpdatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.core.domain.PaymentStatus;
import com.felipe.ecommerce_payment_service.infrastructure.exceptions.InsufficientCustomerBalanceException;
import com.felipe.kafka.saga.commands.PaymentTransactionCancelCommand;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@KafkaListener(id = "payment-transactions", topics = "order.order_transaction.payment.commands", groupId = "payment-service")
public class KafkaService {
  private final PaymentService paymentService;
  private final UpdatePaymentUseCase updatePaymentUseCase;
  private final KafkaTemplate<String, PaymentTransactionReply> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLY_TOPIC = "order.order_transaction.replies";

  public KafkaService(PaymentService paymentService, UpdatePaymentUseCase updatePaymentUseCase,
                      KafkaTemplate<String, PaymentTransactionReply> kafkaTemplate) {
    this.paymentService = paymentService;
    this.updatePaymentUseCase = updatePaymentUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaHandler
  void processPayment(PaymentTransactionCreateCommand paymentCommand) {
    logger.info(
      "\nCommand received in Process payment:\norderId: {}\norderAmount: {}\nproductQuantity: {}\nemail: {}",
      paymentCommand.getOrderId(),
      paymentCommand.getOrderAmount(),
      paymentCommand.getProducts().size(),
      paymentCommand.getCustomer().email()
    );
    PaymentTransactionReply.Builder replyBuilder = PaymentTransactionReply.builder()
      .withSagaId(paymentCommand.getSagaId())
      .withTransactionId(paymentCommand.getTransactionId())
      .withOrderId(paymentCommand.getOrderId())
      .withCommand(paymentCommand.getCommand())
      .withParticipant(PaymentTransactionReply.SagaParticipant.PAYMENT);

    CompletableFuture<SendResult<String, PaymentTransactionReply>> sentMessage;
    try {
      String checkoutUrl = this.paymentService.processPayment(paymentCommand);
      replyBuilder.withCheckoutUrl(checkoutUrl)
        .withOrderAmount(paymentCommand.getOrderAmount())
        .withFailureCode(ReplyTransaction.FailureCode.NO_APPLY)
        .withFailureMessage(null)
        .success();

      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, replyBuilder.build());
    } catch (Exception ex) {
      mapBusinessExceptions(ex, replyBuilder);
      logger.error("Error in process payment reply -> {}", ex.getMessage(), ex);
      sentMessage = this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, replyBuilder.build());
    }
    sentMessage.whenComplete((result, exception) ->
      logger.info(
        "Process payment reply posted on topic \"{}\" successfully -> status: {}",
        result.getRecordMetadata().topic(), result.getProducerRecord().value().getStatus().name()
      ));
  }

  @KafkaHandler
  void cancelPayment(PaymentTransactionCancelCommand paymentCommand) {
    logger.info("Command received in Cancel payment -> orderId: {}", paymentCommand.getOrderId());

    try {
      Payment updatedPayment = this.updatePaymentUseCase.execute(paymentCommand.getOrderId(), PaymentStatus.CANCELLED);
      long orderAmount = this.paymentService.formatBigDecimalStringToValidLongValue(updatedPayment.getOrderAmount().toString());
      // Passing fixedCustomerBalance as a negative value to refund the customer's balance
      long fixedCustomerBalance = -orderAmount;
      this.paymentService.processCustomerBalance(updatedPayment.getStripeCustomerId(), fixedCustomerBalance);
      logger.info("Payment of order \"{}\" cancelled and customer balance refunded", paymentCommand.getOrderId());
    } catch (Exception ex) {
      logger.error("Error in cancel payment reply -> {}", ex.getMessage(), ex);
    }
  }

  private void mapBusinessExceptions(Exception exception, PaymentTransactionReply.Builder replyBuilder) {
    if (exception instanceof InsufficientCustomerBalanceException ex) {
      replyBuilder.withFailureCode(ReplyTransaction.FailureCode.BUSINESS_EXCEPTION)
        .withFailureMessage(ex.getMessage())
        .failure();
    } else {
      replyBuilder.withFailureCode(ReplyTransaction.FailureCode.INFRASTRUCTURE_EXCEPTION)
        .withFailureMessage(exception.getMessage())
        .failure();
    }
  }
}
