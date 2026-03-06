package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public final class CancellingTransition extends SagaTransition {
  private final ReplyTransaction reply;
  private final DeleteOrderUseCase deleteOrderUseCase;
  private static final Logger logger = LoggerFactory.getLogger(CancellingTransition.class);

  public CancellingTransition(ReplyTransaction reply, DeleteOrderUseCase deleteOrderUseCase) {
    this.reply = reply;
    this.deleteOrderUseCase =  deleteOrderUseCase;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> saga.setStatus(SagaStatus.FAILED);
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      Order deletedOrder = this.deleteOrderUseCase.execute(this.reply.getOrderId());
      logger.info("Order with id \"{}\" deleted successfully", deletedOrder.getId());
    };
  }
}
