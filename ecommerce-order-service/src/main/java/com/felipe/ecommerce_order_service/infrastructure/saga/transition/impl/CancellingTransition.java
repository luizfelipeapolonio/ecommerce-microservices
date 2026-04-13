package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.DiscountTransactionCancelCommand;
import com.felipe.kafka.saga.replies.ReplyTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class CancellingTransition extends SagaTransition {
  private final ReplyTransaction reply;
  private final DeleteOrderUseCase deleteOrderUseCase;
  private final CustomerGateway customerGateway;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(CancellingTransition.class);

  public CancellingTransition(ReplyTransaction reply,
                              DeleteOrderUseCase deleteOrderUseCase,
                              CustomerGateway customerGateway,
                              KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.deleteOrderUseCase =  deleteOrderUseCase;
    this.customerGateway = customerGateway;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> saga.setStatus(SagaStatus.FAILED);
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      Order deletedOrder = this.deleteOrderUseCase.execute(this.reply.getOrderId());
      logger.info("Order with id \"{}\" deleted successfully", deletedOrder.getId());

      if (deletedOrder.isWithCoupon()) {
        CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
        UUID transactionId = UUID.randomUUID();
        UUID authCustomerId = UUID.fromString(authCustomer.id());

        DiscountTransactionCancelCommand discountCommand = DiscountTransactionCancelCommand
          .startTransaction(this.reply.getSagaId(), transactionId)
          .withCouponCode(deletedOrder.getCouponCode())
          .withOrderId(deletedOrder.getId())
          .withCustomerId(authCustomerId)
          .build();

        this.kafkaTemplate.send("order.order_transaction.discount.commands", discountCommand)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info("Remove coupon application posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
            }
          });
      }
    };
  }
}
