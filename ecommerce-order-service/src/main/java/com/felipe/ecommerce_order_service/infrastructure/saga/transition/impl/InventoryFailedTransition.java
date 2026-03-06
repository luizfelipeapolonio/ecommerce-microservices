package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class InventoryFailedTransition extends SagaTransition {
  private final InventoryTransactionReply reply;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(InventoryFailedTransition.class);

  public InventoryFailedTransition(InventoryTransactionReply reply, KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantFailed(this.reply.getParticipant());
      saga.setStatus(SagaStatus.CANCELLING);
      saga.setFailureCode(this.reply.getFailureCode());
      saga.setFailureReason(this.reply.getFailureMessage());
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UUID transactionId = UUID.randomUUID();

      InventoryTransactionCancelCommand inventoryCommand = InventoryTransactionCancelCommand.builder(reply.getSagaId(), transactionId)
        .withProductId(reply.getProduct().getId())
        .withOrderId(reply.getOrderId())
        .withFailureCode(reply.getFailureCode())
        .build();

      this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Cancel Order posted in topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    };
  }
}
