package com.felipe.ecommerce_order_service.infrastructure.saga;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.event.SagaEvent;

public interface SagaStateMachine {
  SagaTransition handle(OrderSaga saga, SagaEvent event);
}
