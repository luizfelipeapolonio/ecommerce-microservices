package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentSucceededTransition;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.kafka.core.KafkaTemplate;

public class ProcessingStateHandler implements SagaState {
  private final UpdateOrderUseCase updateOrderUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProcessingStateHandler(UpdateOrderUseCase updateOrderUseCase, KafkaTemplate<String, Object> kafkaTemplate) {
    this.updateOrderUseCase = updateOrderUseCase;
    this.kafkaTemplate = kafkaTemplate;
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
      case FAILURE -> new PaymentFailedTransition(paymentReply, this.kafkaTemplate);
    };
  }
}
