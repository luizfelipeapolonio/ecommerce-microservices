package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.OrderTransitionDataHolder;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.DiscountTransactionReply;
import com.felipe.utils.product.PricingCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class DiscountSucceededTransition extends SagaTransition {
  private final DiscountTransactionReply reply;
  private final CustomerGateway customerGateway;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final OrderTransitionDataHolder orderTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(DiscountSucceededTransition.class);

  public DiscountSucceededTransition(DiscountTransactionReply reply,
                                     CustomerGateway customerGateway,
                                     UpdateOrderUseCase updateOrderUseCase,
                                     OrderTransitionDataHolder orderTransitionDataHolder,
                                     KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.customerGateway = customerGateway;
    this.updateOrderUseCase = updateOrderUseCase;
    this.orderTransitionDataHolder = orderTransitionDataHolder;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> saga.markParticipantSuccess(DiscountTransactionReply.SagaParticipant.DISCOUNT);
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
      UUID authCustomerId = UUID.fromString(authCustomer.id());
      UUID transactionId = UUID.randomUUID();
      List<PaymentTransactionCreateCommand.ProductData> products = this.orderTransitionDataHolder.getProducts()
        .stream()
        .map(productData -> new PaymentTransactionCreateCommand.ProductData(
          productData.name(),
          productData.quantity(),
          productData.unitPrice(),
          productData.discountType(),
          productData.discountValue()
        ))
        .toList();
      String currentOrderAmount = this.orderTransitionDataHolder.getOrderAmount();
      String orderAmount = calculateOrderAmount(currentOrderAmount, this.reply.getDiscountType(), this.reply.getDiscountValue());
      String couponDiscountAmount = calculateDiscount(currentOrderAmount, this.reply.getDiscountType(), this.reply.getDiscountValue());

      // Updating order amount
      UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO().updateOrderPrice(orderAmount);
      this.updateOrderUseCase.execute(this.reply.getOrderId(), updateOrderDTO);

      PaymentTransactionCreateCommand paymentCommand = PaymentTransactionCreateCommand
        .startTransaction(this.reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .withOrderAmount(orderAmount)
        .withShippingFee(this.orderTransitionDataHolder.getShippingFee())
        .withProducts(products)
        .withCustomer(new PaymentTransactionCreateCommand.CustomerData(
          authCustomerId,
          authCustomer.username(),
          authCustomer.email(),
          new PaymentTransactionCreateCommand.CustomerAddress(
            authCustomer.address().street(),
            authCustomer.address().number(),
            authCustomer.address().complement(),
            authCustomer.address().district(),
            authCustomer.address().zipcode(),
            authCustomer.address().city(),
            authCustomer.address().state(),
            authCustomer.address().country()
          )
        ))
        .withCoupon(new PaymentTransactionCreateCommand.CouponData(
          this.reply.getCouponCode(),
          this.reply.getDiscountType(),
          this.reply.getDiscountValue(),
          couponDiscountAmount
        ))
        .build();

      this.kafkaTemplate.send("order.order_transaction.payment.commands", paymentCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Create payment posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    };
  }

  private String calculateOrderAmount(String currentOrderAmount, String couponDiscountType, String couponDiscountValue) {
    return PricingCalculator
      .calculateFinalPrice(couponDiscountType, currentOrderAmount, couponDiscountValue, 1L)
      .toPlainString();
  }

  private String calculateDiscount(String currentOrderAmount, String discountType, String discountValue) {
    return PricingCalculator.calculateDiscount(currentOrderAmount, discountType, discountValue).toPlainString();
  }
}
