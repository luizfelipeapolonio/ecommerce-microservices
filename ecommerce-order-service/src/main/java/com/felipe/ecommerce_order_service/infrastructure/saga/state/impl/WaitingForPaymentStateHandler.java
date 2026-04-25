package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentCompletedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentFailedTransition;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.kafka.core.KafkaTemplate;

public class WaitingForPaymentStateHandler implements SagaState {
  private final UpdateOrderUseCase updateOrderUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public WaitingForPaymentStateHandler(UpdateOrderUseCase updateOrderUseCase, KafkaTemplate<String, Object> kafkaTemplate) {
    this.updateOrderUseCase = updateOrderUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public SagaStatus state() {
    return SagaStatus.WAITING_FOR_PAYMENT;
  }

  @Override
  public SagaTransition handle(OrderSaga saga, ReplyTransaction reply) {
    return switch (reply.getParticipant()){
      case INVENTORY, DISCOUNT, SHIPPING -> throw new UnhandledSagaParticipantException(reply.getParticipant().name(), SagaStatus.WAITING_FOR_PAYMENT);
      case PAYMENT -> handlePaymentFinished(reply);
    };
  }

  private SagaTransition handlePaymentFinished(ReplyTransaction reply) {
    PaymentTransactionReply paymentReply = (PaymentTransactionReply) reply;
    return switch (paymentReply.getStatus()) {
      case SUCCESS -> new PaymentCompletedTransition(paymentReply, this.updateOrderUseCase, this.kafkaTemplate);
      case FAILURE -> new PaymentFailedTransition(paymentReply, this.kafkaTemplate);
    };
  }
}
