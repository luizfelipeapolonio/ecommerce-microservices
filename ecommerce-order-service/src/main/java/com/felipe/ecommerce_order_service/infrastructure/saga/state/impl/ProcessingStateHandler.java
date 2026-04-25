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
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.ShippingFailedTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl.ShippingSucceededTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.OrderTransitionDataHolder;
import com.felipe.kafka.saga.replies.DiscountTransactionReply;
import com.felipe.kafka.saga.replies.PaymentTransactionReply;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import com.felipe.kafka.saga.replies.ShippingTransactionReply;
import org.springframework.kafka.core.KafkaTemplate;

public class ProcessingStateHandler implements SagaState {
  private final UpdateOrderUseCase updateOrderUseCase;
  private final CustomerGateway customerGateway;
  private final OrderTransitionDataHolder orderTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  public ProcessingStateHandler(UpdateOrderUseCase updateOrderUseCase,
                                CustomerGateway customerGateway,
                                OrderTransitionDataHolder orderTransitionDataHolder,
                                KafkaTemplate<String, Object> kafkaTemplate) {
    this.updateOrderUseCase = updateOrderUseCase;
    this.customerGateway = customerGateway;
    this.orderTransitionDataHolder = orderTransitionDataHolder;
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
      case SHIPPING -> handleShippingProcessing(reply);
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
        this.updateOrderUseCase,
        this.orderTransitionDataHolder,
        this.kafkaTemplate
      );
      case FAILURE -> new DiscountFailedTransition(
        discountReply,
        this.customerGateway,
        this.updateOrderUseCase,
        this.orderTransitionDataHolder,
        this.kafkaTemplate
      );
    };
  }

  private SagaTransition handleShippingProcessing(ReplyTransaction reply) {
    ShippingTransactionReply shippingReply = (ShippingTransactionReply) reply;
    return switch (reply.getStatus()) {
      case SUCCESS -> new ShippingSucceededTransition(
        shippingReply,
        this.customerGateway,
        this.updateOrderUseCase,
        this.orderTransitionDataHolder,
        this.kafkaTemplate
      );
      case FAILURE -> new ShippingFailedTransition(reply, this.updateOrderUseCase, this.kafkaTemplate);
    };
  }
}
