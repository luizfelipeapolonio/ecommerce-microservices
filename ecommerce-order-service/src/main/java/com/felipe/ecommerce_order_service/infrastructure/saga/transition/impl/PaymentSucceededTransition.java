package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;

public final class PaymentSucceededTransition extends SagaTransition {
  private final PaymentTransactionReply reply;
  private final UpdateOrderUseCase updateOrderUseCase;
  private static final Logger logger = LoggerFactory.getLogger(PaymentSucceededTransition.class);

  public PaymentSucceededTransition(PaymentTransactionReply reply, UpdateOrderUseCase updateOrderUseCase) {
    this.reply = reply;
    this.updateOrderUseCase = updateOrderUseCase;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      saga.markParticipantProcessing(PaymentTransactionReply.SagaParticipant.PAYMENT);
      saga.setStatus(SagaStatus.WAITING_FOR_PAYMENT);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      UpdateOrderDTO updateDTO = new UpdateOrderDTO(null, this.reply.getCheckoutUrl(), null, this.reply.getOrderAmount(), List.of());
      Order updatedOrder = this.updateOrderUseCase.execute(this.reply.getOrderId(), updateDTO);
      logger.info("Updated order -> checkoutUrl: {}", updatedOrder.getCheckoutUrl());
    };
  }
}
