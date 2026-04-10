package com.felipe.ecommerce_order_service.infrastructure.saga.state.impl;

import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.UnhandledSagaParticipantException;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.state.SagaState;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.DiscountFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.DiscountSucceededTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.PaymentSucceededTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.InventoryTransitionDataHolder;
import com.felipe.kafka.saga.replies.DiscountTransactionReply;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.springframework.kafka.core.KafkaTemplate;

public class ProcessingStateHandler implements SagaState {
  private final UpdateOrderUseCase updateOrderUseCase;
  private final CustomerGateway customerGateway;
  private final InventoryTransitionDataHolder inventoryTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProcessingStateHandler(UpdateOrderUseCase updateOrderUseCase,
                                CustomerGateway customerGateway,
                                InventoryTransitionDataHolder inventoryTransitionDataHolder,
                                KafkaTemplate<String, Object> kafkaTemplate) {
    this.updateOrderUseCase = updateOrderUseCase;
    this.customerGateway = customerGateway;
    this.inventoryTransitionDataHolder = inventoryTransitionDataHolder;
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
      case DISCOUNT -> handleDiscountProcessing(reply);
    };
  }

  private SagaTransition handlePaymentProcessing(ReplyTransaction reply) {
    PaymentTransactionReply paymentReply = (PaymentTransactionReply) reply;
    return switch (reply.getStatus()){
      case SUCCESS -> new PaymentSucceededTransition(paymentReply, this.updateOrderUseCase);
      case FAILURE -> new PaymentFailedTransition(paymentReply, this.kafkaTemplate);
    };
  }

  private SagaTransition handleDiscountProcessing(ReplyTransaction reply) {
    DiscountTransactionReply discountReply = (DiscountTransactionReply) reply;
    return switch (reply.getStatus()) {
      case SUCCESS -> new DiscountSucceededTransition(
        discountReply,
        this.customerGateway,
        this.inventoryTransitionDataHolder,
        this.kafkaTemplate
      );
      case FAILURE -> new DiscountFailedTransition(
        discountReply,
        this.customerGateway,
        this.inventoryTransitionDataHolder,
        this.kafkaTemplate
      );
    };
  }
}
