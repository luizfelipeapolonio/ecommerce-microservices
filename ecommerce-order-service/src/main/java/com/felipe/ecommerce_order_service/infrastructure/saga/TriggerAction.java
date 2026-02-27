package com.felipe.ecommerce_order_service.infrastructure.saga;

@FunctionalInterface
public interface TriggerAction {
  void trigger();
}
