package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.InventoryFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.InventorySucceededTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.OrderTransitionDataHolder;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.kafka.core.KafkaTemplate;

public class StartedStateHandler implements SagaState {
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final OrderTransitionDataHolder orderTransitionDataHolder;

  public StartedStateHandler(KafkaTemplate<String, Object> kafkaTemplate, UpdateOrderUseCase updateOrderUseCase,
                             OrderTransitionDataHolder orderTransitionDataHolder) {
    this.kafkaTemplate = kafkaTemplate;
    this.updateOrderUseCase = updateOrderUseCase;
    this.orderTransitionDataHolder = orderTransitionDataHolder;
  }

  @Override
  public SagaStatus state() {
    return SagaStatus.STARTED;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, ReplyTransaction reply) {
    return switch (reply.getParticipant()) {
      case INVENTORY -> handleInventoryStarted(reply);
      case PAYMENT, DISCOUNT, SHIPPING -> throw new UnhandledSagaParticipantException(reply.getParticipant().name(), SagaStatus.STARTED);
    };
  }

  private SagaTransition handleInventoryStarted(ReplyTransaction reply) {
    InventoryTransactionReply inventoryReply = (InventoryTransactionReply) reply;
    return switch (reply.getStatus()) {
      case SUCCESS -> new InventorySucceededTransition(
        inventoryReply,
        this.updateOrderUseCase,
        this.orderTransitionDataHolder,
        this.kafkaTemplate
      );
      case FAILURE -> new InventoryFailedTransition(inventoryReply, this.kafkaTemplate);
    };
  }
}
