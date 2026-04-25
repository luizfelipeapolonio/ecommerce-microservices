package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public final class InventoryCommitSucceededTransition extends SagaTransition {
  private final ReplyTransaction reply;
  private final UpdateOrderUseCase updateOrderUseCase;
  private static final Logger logger = LoggerFactory.getLogger(InventoryCommitSucceededTransition.class);

  public InventoryCommitSucceededTransition(ReplyTransaction reply, UpdateOrderUseCase updateOrderUseCase) {
    this.reply = reply;
    this.updateOrderUseCase = updateOrderUseCase;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantSuccess(ReplyTransaction.SagaParticipant.INVENTORY);
      saga.setStatus(SagaStatus.COMPLETED);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UpdateOrderDTO orderDTO = new UpdateOrderDTO().updateStatus(OrderStatus.FINISHED);
      Order updatedOrder = this.updateOrderUseCase.execute(this.reply.getOrderId(), orderDTO);
      logger.info("Order \"{}\" finished successfully", updatedOrder.getId());
      logger.info("Saga \"{}\" completed successfully", this.reply.getSagaId());
    };
  }
}
