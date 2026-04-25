package com.felipe.ecommerce_order_service.infrastructure.saga.transition.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.UpdateProductDTO;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;
import com.felipe.ecommerce_order_service.infrastructure.saga.transition.SagaTransition;
import com.felipe.ecommerce_order_service.infrastructure.saga.utils.OrderTransitionDataHolder;
import com.felipe.kafka.saga.commands.ShippingTransactionCreateCommand;
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
  private final UpdateOrderUseCase updateOrderUseCase;
  private final OrderTransitionDataHolder orderTransitionDataHolder;
  private final KafkaTemplate<String, Object> kafkaTemplate;
  private static final Logger logger = LoggerFactory.getLogger(InventorySucceededTransition.class);

  public InventorySucceededTransition(InventoryTransactionReply reply,
                                      UpdateOrderUseCase updateOrderUseCase,
                                      OrderTransitionDataHolder orderTransitionDataHolder,
                                      KafkaTemplate<String, Object> kafkaTemplate) {
    this.reply = reply;
    this.updateOrderUseCase = updateOrderUseCase;
    this.orderTransitionDataHolder = orderTransitionDataHolder;
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
      UUID transactionId = UUID.randomUUID();
      // Updating order data
      String orderAmount = calculateOrderAmount(this.reply.getProducts());
      UpdateOrderDTO updateOrderDTO = convertToUpdateOrderDTO(this.reply, orderAmount);
      Order updatedOrder = this.updateOrderUseCase.execute(this.reply.getOrderId(), updateOrderDTO);

      // Setting products data in the helper context
      this.orderTransitionDataHolder.setProducts(this.reply.getProducts());
      this.orderTransitionDataHolder.setCouponCode(updatedOrder.getCouponCode());
      this.orderTransitionDataHolder.setOrderAmount(orderAmount);

      ShippingTransactionCreateCommand shippingCommand = ShippingTransactionCreateCommand
        .startTransaction(this.reply.getSagaId(), transactionId)
        .withOrderId(this.reply.getOrderId())
        .build();

      this.kafkaTemplate.send("order.order_transaction.shipping.commands", shippingCommand)
        .whenComplete((result, exception) -> {
          if (exception == null) {
            logger.info("Calculate shipping fee command posted on topic \"{}\" successfully", result.getRecordMetadata().topic());
          }
        });
    };
  }

  private String calculateOrderAmount(List<InventoryTransactionReply.ProductData> products) {
    BigDecimal orderAmount = new BigDecimal("0.00");
    for (var product : products) {
      BigDecimal price = PricingCalculator.calculateFinalPrice(
        product.getDiscountType(),
        product.getUnitPrice(),
        product.getDiscountValue(),
        product.getQuantity()
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
    return new UpdateOrderDTO().updateOrderPrice(orderAmount).updateProducts(productsDTO);
  }
}
