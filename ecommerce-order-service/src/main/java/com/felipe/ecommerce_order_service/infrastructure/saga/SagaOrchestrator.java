package com.felipe.ecommerce_order_service.infrastructure.saga;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SagaOrchestrator {
  private final OrderSagaService orderSagaService;
  private final Map<SagaStatus, SagaState> handlers = new HashMap<>();

  public SagaOrchestrator(OrderSagaService orderSagaService, List<SagaState> sagaStates) {
    this.orderSagaService = orderSagaService;
    sagaStates.forEach(sagaState -> this.handlers.put(sagaState.state(), sagaState));
  }

  public void handle(OrderSaga saga, ReplyTransaction reply) {
    SagaState state = this.handlers.get(saga.getStatus());
    SagaTransition transition = state.handle(saga, reply);
    transition.applyChanges(saga);
    this.orderSagaService.updateOrderSaga(saga);
    transition.triggerAction();
  }
}
