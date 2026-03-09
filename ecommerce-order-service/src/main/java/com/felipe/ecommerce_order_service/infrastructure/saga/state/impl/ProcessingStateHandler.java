package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentSucceededTransition;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;

public class ProcessingStateHandler implements SagaState {
  private final UpdateOrderUseCase updateOrderUseCase;

  public ProcessingStateHandler(UpdateOrderUseCase updateOrderUseCase) {
    this.updateOrderUseCase = updateOrderUseCase;
  }

  @Override
  public SagaStatus state() {
    return SagaStatus.PROCESSING;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, ReplyTransaction reply) {
    return switch (reply.getParticipant()) {
      case INVENTORY -> throw new UnhandledSagaParticipantException(reply.getParticipant().name(), SagaStatus.PROCESSING);
      case PAYMENT -> handlePaymentProcessing(reply);
    };
  }

  private SagaTransition handlePaymentProcessing(ReplyTransaction reply) {
    PaymentTransactionReply paymentReply = (PaymentTransactionReply) reply;
    return switch (reply.getStatus()){
      case SUCCESS -> new PaymentSucceededTransition(paymentReply, this.updateOrderUseCase);
      case FAILURE -> null;
    };
  }
}
