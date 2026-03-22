package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import com.felipe.kafka.saga.commands.PaymentTransactionCancelCommand;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class InventoryCommitFailedTransition extends SagaTransition {
  private final ReplyTransaction reply;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(InventoryCommitFailedTransition.class);

  public InventoryCommitFailedTransition(ReplyTransaction reply, KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantFailed(ReplyTransaction.SagaParticipant.INVENTORY);
      saga.setStatus(SagaStatus.CANCELLING);
      saga.setFailureCode(this.reply.getFailureCode());
      saga.setFailureReason(this.reply.getFailureMessage());
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UUID sagaId = this.reply.getSagaId();
      UUID transactionId = UUID.randomUUID();

      InventoryTransactionCancelCommand inventoryCommand = InventoryTransactionCancelCommand.builder(sagaId, transactionId)
        .withOrderId(this.reply.getOrderId())
        .withFailureCode(this.reply.getFailureCode())
        .build();

      PaymentTransactionCancelCommand paymentCommand = PaymentTransactionCancelCommand.builder(sagaId, transactionId)
        .withOrderId(this.reply.getOrderId())
        .build();

      this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Inventory commit cancel command posted successfully");
          }
        });
      this.kafkaTemplate.send("order.order_transaction.payment.commands", paymentCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Payment commit cancel command posted successfully");
          }
        });
    };
  }
}
