package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.InventoryTransactionCancelCommand;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class ShippingFailedTransition extends SagaTransition {
  private final ReplyTransaction reply;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(ShippingFailedTransition.class);

  public ShippingFailedTransition(ReplyTransaction reply, UpdateOrderUseCase updateOrderUseCase,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.updateOrderUseCase = updateOrderUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      String participantDetails = "[%s] %s".formatted(this.reply.getFailureCode(), this.reply.getFailureMessage());
      saga.setStatus(SagaStatus.CANCELLING);
      saga.markParticipantFailed(ReplyTransaction.SagaParticipant.SHIPPING);
      saga.setParticipantDetails(ReplyTransaction.SagaParticipant.SHIPPING, participantDetails);
      saga.setFailureCode(this.reply.getFailureCode());
      saga.setFailureReason(this.reply.getFailureMessage());
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UUID sagaId = this.reply.getSagaId();
      UUID transactionId = UUID.randomUUID();

      UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO().updateWithCoupon(false);
      this.updateOrderUseCase.execute(this.reply.getOrderId(), updateOrderDTO);

      InventoryTransactionCancelCommand inventoryCommand = InventoryTransactionCancelCommand
        .startTransaction(sagaId, transactionId)
        .withOrderId(this.reply.getOrderId())
        .withFailureCode(this.reply.getFailureCode())
        .build();

      this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Inventory cancel command posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    };
  }
}
