package com.felipe.ecommerce_order_service.infrastructure.saga.transition;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;

import java.util.function.Consumer;

public abstract class SagaTransition {
  protected abstract Consumer<OrderSaga> sagaMutation();
  protected abstract TriggerAction action();

  @FunctionalInterface
  public interface TriggerAction {
    void trigger();
  }

  public final void applyChanges(OrderSaga saga) {
    sagaMutation().accept(saga);
  }

  public final void triggerAction() {
    action().trigger();
  }
}
