package com.felipe.ecommerce_payment_service.infrastructure.external;

import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
  private final PaymentService paymentService;
  private final KafkaTemplate<String, PaymentTransactionReply> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);
  private static final String ORDER_TRANSACTION_REPLY_TOPIC = "order.order_transaction.replies";

  public KafkaService(PaymentService paymentService, KafkaTemplate<String, PaymentTransactionReply> kafkaTemplate) {
    this.paymentService = paymentService;
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(topics = "order.order_transaction.payment.commands", groupId = "payment-service")
  void processPayment(PaymentTransactionCreateCommand paymentCommand) {
    logger.info(
      "\nCommand received in Process payment:\norderId: {}\norderAmount: {}\nproductName: {}\nemail: {}",
      paymentCommand.getOrderId(),
      paymentCommand.getOrderAmount(),
      paymentCommand.getProductName(),
      paymentCommand.getCustomer().email()
    );
    try {
      String checkoutUrl = this.paymentService.processPayment(paymentCommand);

      PaymentTransactionReply paymentReply = PaymentTransactionReply.builder()
        .withSagaId(paymentCommand.getSagaId())
        .withTransactionId(paymentCommand.getTransactionId())
        .withOrderId(paymentCommand.getOrderId())
        .withCommand(paymentCommand.getCommand())
        .withParticipant(PaymentTransactionReply.SagaParticipant.PAYMENT)
        .withCheckoutUrl(checkoutUrl)
        .success()
        .build();

      this.kafkaTemplate.send(ORDER_TRANSACTION_REPLY_TOPIC, paymentReply)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Process payment posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    } catch (Exception ex) {
      logger.error("Error in process payment reply -> {}", ex.getMessage(), ex);
    }
  }
}
