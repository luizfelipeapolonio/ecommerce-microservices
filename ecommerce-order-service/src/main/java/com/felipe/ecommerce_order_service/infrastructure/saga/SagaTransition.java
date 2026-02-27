package com.felipe.ecommerce_order_service.infrastructure.saga;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;

import java.util.function.Consumer;

public record SagaTransition(Consumer<OrderSaga> stateMutation, TriggerAction action) {
  public void apply(OrderSaga saga) {
    stateMutation.accept(saga);
    action.trigger();
  }
}
