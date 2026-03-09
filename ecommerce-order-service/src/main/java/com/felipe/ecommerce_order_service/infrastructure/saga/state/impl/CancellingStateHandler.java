package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.CancellingTransition;
import com.felipe.kafka.saga.replies.ReplyTransaction;

public class CancellingStateHandler implements SagaState {
  private final DeleteOrderUseCase deleteOrderUseCase;

  public CancellingStateHandler(DeleteOrderUseCase deleteOrderUseCase) {
    this.deleteOrderUseCase = deleteOrderUseCase;
  }

  @Override
  public SagaStatus state() {
    return SagaStatus.CANCELLING;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, ReplyTransaction reply) {
    return switch (reply.getParticipant()){
      case INVENTORY -> handleInventoryCancelling(reply);
      case PAYMENT -> throw new UnhandledSagaParticipantException(reply.getParticipant().name(), SagaStatus.CANCELLING);
    };
  }

  private SagaTransition handleInventoryCancelling(ReplyTransaction reply) {
    return new CancellingTransition(reply, this.deleteOrderUseCase);
  }
}
