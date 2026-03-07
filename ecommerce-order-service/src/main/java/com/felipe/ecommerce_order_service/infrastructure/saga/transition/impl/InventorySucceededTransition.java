package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.utils.product.PricingCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.function.Consumer;

public final class InventorySucceededTransition extends SagaTransition {
  private final InventoryTransactionReply reply;
  private final CustomerGateway customerGateway;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(InventorySucceededTransition.class);

  public InventorySucceededTransition(InventoryTransactionReply reply, CustomerGateway customerGateway,
                                      KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.customerGateway = customerGateway;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return orderSaga -> {
      orderSaga.markParticipantSuccess(this.reply.getParticipant());
      orderSaga.setStatus(SagaStatus.PROCESSING);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
      logger.info("Authenticated customer -> id: {} - email: {}", authCustomer.id(), authCustomer.email());
      UUID transactionId = UUID.randomUUID();
      InventoryTransactionReply.ProductData product = reply.getProduct();

      PaymentTransactionCreateCommand paymentCommand = PaymentTransactionCreateCommand.builder(reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .withOrderAmount(calculateOrderAmount(product))
        .withProduct(new PaymentTransactionCreateCommand.ProductData(
          product.getName(),
          product.getQuantity(),
          product.getUnitPrice(),
          product.getDiscountType(),
          product.getDiscountValue()
        ))
        .withCustomer(new PaymentTransactionCreateCommand.CustomerData(
          UUID.fromString(authCustomer.id()),
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

  private String calculateOrderAmount(InventoryTransactionReply.ProductData product) {
    return PricingCalculator.calculateFinalPrice(
      product.getDiscountType(),
      product.getUnitPrice(),
      product.getDiscountValue(),
      (int) product.getQuantity()
    ).toString();
  }
}
