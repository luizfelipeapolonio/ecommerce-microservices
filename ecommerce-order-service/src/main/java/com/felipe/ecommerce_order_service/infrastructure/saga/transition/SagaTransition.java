package com.felipe.ecommerce_order_service.infrastructure.saga.transition;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;

import java.util.function.Consumer;

public abstract class SagaTransition {
  private final Consumer<OrderSaga> sagaMutation = sagaMutation();
  private final TriggerAction action = action();

  @FunctionalInterface
  public interface TriggerAction {
    void trigger();
  }

  protected abstract Consumer<OrderSaga> sagaMutation();
  protected abstract TriggerAction action();

  public final void apply(OrderSaga saga) {
    this.sagaMutation.accept(saga);
    this.action.trigger();
  }
}
