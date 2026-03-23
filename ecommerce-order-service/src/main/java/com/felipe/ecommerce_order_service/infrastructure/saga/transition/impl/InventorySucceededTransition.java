package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateProductDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.kafka.saga.commands.PaymentTransactionCreateCommand;
import com.felipe.kafka.saga.replies.InventoryTransactionReply;
import com.felipe.utils.product.PricingCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public final class InventorySucceededTransition extends SagaTransition {
  private final InventoryTransactionReply reply;
  private final CustomerGateway customerGateway;
  private final UpdateOrderUseCase updateOrderUseCase;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(InventorySucceededTransition.class);

  public InventorySucceededTransition(InventoryTransactionReply reply,
                                      CustomerGateway customerGateway,
                                      UpdateOrderUseCase updateOrderUseCase,
                                      KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.customerGateway = customerGateway;
    this.updateOrderUseCase = updateOrderUseCase;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  protected Consumer<OrderSaga> sagaMutation() {
    return orderSaga -> {
      orderSaga.markParticipantProcessing(InventoryTransactionReply.SagaParticipant.INVENTORY);
      orderSaga.setStatus(SagaStatus.PROCESSING);
    };
  }

  @Override
  protected TriggerAction action() {
    return () -> {
      CustomerProfileDTO authCustomer = this.customerGateway.getCurrentAuthCustomer();
      logger.info("Authenticated customer -> id: {} - email: {}", authCustomer.id(), authCustomer.email());
      UUID transactionId = UUID.randomUUID();
      List<PaymentTransactionCreateCommand.ProductData> products = this.reply.getProducts()
        .stream()
        .map(productData -> new PaymentTransactionCreateCommand.ProductData(
          productData.getName(),
          productData.getQuantity(),
          productData.getUnitPrice(),
          productData.getDiscountType(),
          productData.getDiscountValue()
        ))
        .toList();
      String orderAmount = calculateOrderAmount(products);
      // Updating order data
      UpdateOrderDTO updateOrderDTO = convertToUpdateOrderDTO(this.reply, orderAmount);
      this.updateOrderUseCase.execute(this.reply.getOrderId(), updateOrderDTO);

      PaymentTransactionCreateCommand paymentCommand = PaymentTransactionCreateCommand.builder(reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .withOrderAmount(orderAmount)
        .withProducts(products)
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

  private UpdateOrderDTO convertToUpdateOrderDTO(InventoryTransactionReply reply, String orderAmount) {
    List<UpdateProductDTO> productsDTO = reply.getProducts()
      .stream()
      .map(product -> {
        BigDecimal finalPrice = PricingCalculator.calculateFinalPrice(
          product.getDiscountType(),
          product.getUnitPrice(),
          product.getDiscountValue(),
          product.getQuantity()
        );
        return new UpdateProductDTO(product.getId(), product.getName(), finalPrice);
      })
      .toList();
    return new UpdateOrderDTO(null, null, null, orderAmount, productsDTO);
  }
}
