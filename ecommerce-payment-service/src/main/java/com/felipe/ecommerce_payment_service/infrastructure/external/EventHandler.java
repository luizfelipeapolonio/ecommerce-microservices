package com.felipe.ecommerce_payment_service.infrastructure.external;

import com.felipe.ecommerce_payment_service.core.application.usecases.UpdatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.core.domain.PaymentStatus;
import com.felipe.ecommerce_payment_service.infrastructure.exceptions.PaymentEventsException;
import com.felipe.kafka.EmailDTO;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.Invoice;
import com.stripe.model.StripeObject;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class EventHandler {

  @Value("${stripe.webhook.key}")
  private String webhookKey;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final UpdatePaymentUseCase updatePaymentUseCase;
  private final PaymentService paymentService;
  private final TaskScheduler taskScheduler;
  private static final Logger logger = LoggerFactory.getLogger(EventHandler.class);
  private static final String ORDER_TRANSACTION_REPLY_TOPIC = "order.order_transaction.replies";

  public EventHandler(KafkaTemplate<String, Object> kafkaTemplate,
                      UpdatePaymentUseCase updatePaymentUseCase,
                      PaymentService paymentService,
                      TaskScheduler taskScheduler) {
    this.kafkaTemplate = kafkaTemplate;
    this.updatePaymentUseCase = updatePaymentUseCase;
    this.paymentService = paymentService;
    this.taskScheduler = taskScheduler;
  }

  public void handle(String payload, String stripeHeader) {
    Event event;
    try {
      event = Webhook.constructEvent(payload, stripeHeader, this.webhookKey);
    } catch (SignatureVerificationException ex) {
      logger.error("Signature exception -> message: {}", ex.getMessage(), ex);
      throw new PaymentEventsException("Error while trying to verify webhook signature");
    }
    EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
    StripeObject stripeObject = dataObjectDeserializer.getObject()
      .orElseThrow(() -> new PaymentEventsException("Error while trying to deserialize StripeObject"));

    switch (event.getType()) {
      case "invoice.paid" -> {
        Invoice invoice = (Invoice) stripeObject;
        String invoiceUrl = invoice.getHostedInvoiceUrl();
        UUID orderId = UUID.fromString(invoice.getMetadata().get("order_id"));

        this.paymentService.processCustomerBalance(invoice.getCustomer(), invoice.getTotal());
        Payment updatedPayment = this.updatePaymentUseCase.execute(orderId, PaymentStatus.PAID);

        PaymentTransactionReply paymentReply = PaymentTransactionReply.builder()
          .withSagaId(updatedPayment.getSagaId())
          .withOrderId(orderId)
          .withCommand(PaymentTransactionReply.Command.CREATE)
          .withTransactionId(updatedPayment.getTransactionId())
          .withParticipant(PaymentTransactionReply.SagaParticipant.PAYMENT)
          .withInvoiceUrl(invoiceUrl)
          .success()
          .build();

        this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, paymentReply)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info(
                "Payment confirmation of order '{}' posted on topic \"{}\" successfully",
                orderId, result.getRecordMetadata().topic()
              );
            }
          });

        EmailDTO emailDTO = new EmailDTO()
          .setOrderId(orderId)
          .setEmailTo(updatedPayment.getCustomerEmail())
          .setSubject(EmailDTO.Subject.APPROVED_PAYMENT);

        this.kafkaTemplate.send("order.emails", emailDTO)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info("Approved payment email posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
            }
          });

        // Delaying invoice creation to simulate a separate event
        this.taskScheduler.schedule(() -> {
          emailDTO.setInvoiceUrl(invoiceUrl);
          emailDTO.setSubject(EmailDTO.Subject.CREATED_INVOICE);

          this.kafkaTemplate.send("order.emails", emailDTO)
            .whenComplete((result, exception) -> {
              if (exception == null) {
                logger.info("Created invoice email posted successfully");
              }
            });
        }, Instant.now().plusSeconds(10));
      }
      case "invoice.payment_failed" -> {
        Invoice invoice = (Invoice) stripeObject;
        String orderId = invoice.getMetadata().get("order_id");
        Payment updatedPayment = this.updatePaymentUseCase.execute(UUID.fromString(orderId), PaymentStatus.CANCELLED);

        PaymentTransactionReply paymentReply = PaymentTransactionReply.builder()
          .withTransactionId(updatedPayment.getTransactionId())
          .withSagaId(updatedPayment.getSagaId())
          .withOrderId(UUID.fromString(orderId))
          .withParticipant(PaymentTransactionReply.SagaParticipant.PAYMENT)
          .withCommand(PaymentTransactionReply.Command.CREATE)
          .withFailureCode(PaymentTransactionReply.FailureCode.INFRASTRUCTURE_EXCEPTION)
          .withFailureMessage("Ocorreu um erro ao confirmar pagamento")
          .failure()
          .build();

        this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, paymentReply)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info(
                "Payment failure of order '{}' posted on topic \"{}\" successfully",
                orderId, result.getRecordMetadata().topic()
              );
            }
          });
      }
      case "payment_intent.succeeded" -> logger.debug("Payment Intent succeeded");
      case "payment_intent.payment_failed" -> logger.debug("Payment Intent failed");
      default -> logger.debug("Unhandled event type: {}", event.getType());
    }
  }
}
