package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.OrderTransitionDataHolder;
import com.felipe.kafka.saga.commands.DiscountTransactionCreateCommand;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.ShippingTransactionReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class ShippingSucceededTransition extends SagaTransition {
  private final ShippingTransactionReply reply;
  private final CustomerGateway customerGateway;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final OrderTransitionDataHolder orderTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(ShippingSucceededTransition.class);

  public ShippingSucceededTransition(ShippingTransactionReply reply,
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
    return saga -> saga.markParticipantSuccess(ShippingTransactionReply.SagaParticipant.SHIPPING);
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
      logger.info("Authenticated customer -> id: {} - email: {}", authCustomer.id(), authCustomer.email());
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
      boolean isWithCoupon = this.orderTransitionDataHolder.isWithCoupon();
      String currentOrderAmount = this.orderTransitionDataHolder.getOrderAmount();
      String orderAmount = calculateOrderAmount(currentOrderAmount, this.reply.getShippingFee());

      // Updating order amount and setting the helper context
      UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO().updateOrderPrice(orderAmount);
      this.updateOrderUseCase.execute(this.reply.getOrderId(), updateOrderDTO);
      this.orderTransitionDataHolder.setOrderAmount(orderAmount);

      // If any product already has a discount, there's no need to trigger the
      // discount service to apply the coupon
      boolean isAllProductsWithNoDiscount = products.stream()
        .allMatch(product -> product.discountType() == null);

      if (isWithCoupon && isAllProductsWithNoDiscount) {
        // Setting the shipping fee in the helper context
        this.orderTransitionDataHolder.setShippingFee(this.reply.getShippingFee());

        DiscountTransactionCreateCommand discountCommand = DiscountTransactionCreateCommand
          .startTransaction(this.reply.getSagaId(), transactionId)
          .withCouponCode(this.orderTransitionDataHolder.getCouponCode())
          .withOrderAmount(orderAmount)
          .withCustomerId(authCustomerId)
          .withOrderId(this.reply.getOrderId())
          .build();

        this.kafkaTemplate.send("order.order_transaction.discount.commands", discountCommand)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info("Apply coupon command posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
            }
          });
      } else {
        PaymentTransactionCreateCommand paymentCommand = PaymentTransactionCreateCommand
          .startTransaction(this.reply.getSagaId(), transactionId)
          .withOrderId(this.reply.getOrderId())
          .withOrderAmount(orderAmount)
          .withShippingFee(this.reply.getShippingFee())
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
          .build();

        this.kafkaTemplate.send("order.order_transaction.payment.commands", paymentCommand)
          .whenComplete((result, exception) -> {
            if (exception == null) {
              logger.info("Create payment posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
            }
          });
      }
    };
  }

  private String calculateOrderAmount(String currentOrderAmount, String shippingFee) {
    return new BigDecimal(currentOrderAmount).add(new BigDecimal(shippingFee)).toPlainString();
  }
}
