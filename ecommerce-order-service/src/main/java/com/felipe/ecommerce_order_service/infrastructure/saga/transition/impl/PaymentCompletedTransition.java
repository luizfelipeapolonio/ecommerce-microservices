package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.InventoryTransactionCommitCommand;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class PaymentCompletedTransition extends SagaTransition {
  private final PaymentTransactionReply reply;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(PaymentCompletedTransition.class);

  public PaymentCompletedTransition(PaymentTransactionReply reply, UpdateOrderUseCase updateOrderUseCase,
                                    KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.updateOrderUseCase = updateOrderUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantSuccess(PaymentTransactionReply.SagaParticipant.PAYMENT);
      saga.setStatus(SagaStatus.COMMITING);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UpdateOrderDTO updateDTO = new UpdateOrderDTO(null, null, this.reply.getInvoiceUrl(), null, List.of());
      Order updatedOrder = this.updateOrderUseCase.execute(this.reply.getOrderId(), updateDTO);
      logger.info("Updated order -> invoiceUrl: {}", updatedOrder.getInvoiceUrl());
      UUID transactionId = UUID.randomUUID();

      InventoryTransactionCommitCommand inventoryCommand = InventoryTransactionCommitCommand
        .startTransaction(this.reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .build();

      this.kafkaTemplate.send("order.order_transaction.inventory.commands", inventoryCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Inventory commit command posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    };
  }
}
