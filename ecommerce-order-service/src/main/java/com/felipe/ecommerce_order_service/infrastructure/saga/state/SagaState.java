package com.felipe.ecommerce_order_service.infrastructure.saga.state;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.replies.ReplyTransaction;

public interface SagaState {
  SagaStatus state();
  SagaTransition handle(OrderSaga saga, ReplyTransaction reply);
}
