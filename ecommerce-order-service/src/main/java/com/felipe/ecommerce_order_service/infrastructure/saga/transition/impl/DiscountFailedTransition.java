package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.InventoryTransitionDataHolder;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.DiscountTransactionReply;
import com.felipe.utils.product.PricingCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class DiscountFailedTransition extends SagaTransition {
  private final DiscountTransactionReply reply;
  private final CustomerGateway customerGateway;
  private final InventoryTransitionDataHolder inventoryTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(DiscountFailedTransition.class);

  public DiscountFailedTransition(DiscountTransactionReply reply,
                                  CustomerGateway customerGateway,
                                  InventoryTransitionDataHolder inventoryTransitionDataHolder,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.customerGateway = customerGateway;
    this.inventoryTransitionDataHolder = inventoryTransitionDataHolder;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return saga -> {
      String participantDetails = "[%s] %s".formatted(this.reply.getFailureCode(), this.reply.getFailureMessage());
      saga.markParticipantFailed(DiscountTransactionReply.SagaParticipant.DISCOUNT);
      saga.setParticipantDetails(DiscountTransactionReply.SagaParticipant.DISCOUNT, participantDetails);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      logger.error(
        "Apply coupon failed -> orderId: {} - failureCode: {} - failureMessage: {}",
        this.reply.getOrderId(), this.reply.getFailureCode(), this.reply.getFailureMessage()
      );

      CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
      logger.info("Authenticated customer -> id: {} - email: {}", authCustomer.id(), authCustomer.email());
      UUID transactionId = UUID.randomUUID();
      UUID authCustomerId = UUID.fromString(authCustomer.id());
      List<PaymentTransactionCreateCommand.ProductData> products = this.inventoryTransitionDataHolder.getProducts()
        .stream()
        .map(productData -> new PaymentTransactionCreateCommand.ProductData(
          productData.name(),
          productData.quantity(),
          productData.unitPrice(),
          productData.discountType(),
          productData.discountValue()
        ))
        .toList();
      String orderAmount = calculateOrderAmount(products);

      PaymentTransactionCreateCommand paymentCommand = PaymentTransactionCreateCommand
        .startTransaction(this.reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .withOrderAmount(orderAmount)
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
    };
  }

  private String calculateOrderAmount(List<PaymentTransactionCreateCommand.ProductData> products) {
    BigDecimal orderAmount = new BigDecimal("0.00");
    for (var product : products) {
      BigDecimal price = PricingCalculator.calculateFinalPrice(
        product.discountType(),
        product.unitPrice(),
        product.discountValue(),
        product.quantity()
      );
      orderAmount = orderAmount.add(price);
    }
    return orderAmount.toString();
  }
}
