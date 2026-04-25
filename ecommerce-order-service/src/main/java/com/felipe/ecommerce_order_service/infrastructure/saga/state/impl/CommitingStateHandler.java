package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.InventoryCommitFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.InventoryCommitSucceededTransition;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.kafka.core.KafkaTemplate;

public class CommitingStateHandler implements SagaState {
  private final UpdateOrderUseCase updateOrderUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public CommitingStateHandler(UpdateOrderUseCase updateOrderUseCase, KafkaTemplate<String, Object> kafkaTemplate) {
    this.updateOrderUseCase = updateOrderUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public SagaStatus state() {
    return SagaStatus.COMMITING;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, ReplyTransaction reply) {
    return switch (reply.getParticipant()) {
      case INVENTORY -> handleInventoryCommiting(reply);
      case PAYMENT, DISCOUNT, SHIPPING -> throw new UnhandledSagaParticipantException(reply.getParticipant().name(), SagaStatus.COMMITING);
    };
  }

  private SagaTransition handleInventoryCommiting(ReplyTransaction reply) {
    return switch (reply.getStatus()) {
      case SUCCESS -> new InventoryCommitSucceededTransition(reply, this.updateOrderUseCase);
      case FAILURE -> new InventoryCommitFailedTransition(reply, this.kafkaTemplate);
    };
  }
}
