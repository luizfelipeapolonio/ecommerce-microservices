package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class PaymentFailedTransition extends SagaTransition {
  private final PaymentTransactionReply reply;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(PaymentFailedTransition.class);

  public PaymentFailedTransition(PaymentTransactionReply reply, KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantFailed(PaymentTransactionReply.SagaParticipant.PAYMENT);
      saga.setStatus(SagaStatus.CANCELLING);
      saga.setFailureCode(this.reply.getFailureCode());
      saga.setFailureReason(this.reply.getFailureMessage());
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UUID transactionId = UUID.randomUUID();
      UUID sagaId = this.reply.getSagaId();

      InventoryTransactionCancelCommand inventoryCommand = InventoryTransactionCancelCommand.builder(sagaId, transactionId)
        .withFailureCode(this.reply.getFailureCode())
        .withOrderId(this.reply.getOrderId())
        .build();

      this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Cancel command posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    };
  }
}
