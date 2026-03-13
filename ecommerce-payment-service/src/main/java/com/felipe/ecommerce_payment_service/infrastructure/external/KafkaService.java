package com.felipe.ecommerce_payment_service.infrastructure.external;

import com.felipe.ecommerce_payment_service.infrastructure.exceptions.InsufficientCustomerBalanceException;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

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
