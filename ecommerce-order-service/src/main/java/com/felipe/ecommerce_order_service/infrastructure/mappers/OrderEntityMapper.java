package com.felipe.ecommerce_order_service.infrastructure.mappers;

import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityMapper {
  public Order toDomain(OrderEntity entity) {
    Order order = new Order()
      .id(entity.getId())
      .customerId(entity.getCustomerId())
      .orderPrice(entity.getOrderPrice())
      .status(OrderStatus.of(entity.getStatus()))
      .withCoupon(entity.isWithCoupon())
      .couponCode(entity.getCouponCode())
      .checkoutUrl(entity.getCheckoutUrl())
      .invoiceUrl(entity.getInvoiceUrl())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt());
    entity.getItems().forEach(itemEntity -> {
      OrderItem orderItem = toOrderItemDomain(itemEntity);
      order.addItem(orderItem);
    });
    return order;
  }

  public OrderEntity toEntity(Order order) {
    OrderEntity orderEntity = new OrderEntity()
      .id(order.getId())
      .customerId(order.getCustomerId())
      .orderPrice(order.getOrderPrice())
      .status(OrderStatus.of(order.getStatus()))
      .withCoupon(order.isWithCoupon())
      .couponCode(order.getCouponCode())
      .checkoutUrl(order.getCheckoutUrl())
      .invoiceUrl(order.getInvoiceUrl())
      .createdAt(order.getCreatedAt())
      .updatedAt(order.getUpdatedAt());
    order.getItems().forEach(item -> {
      OrderItemEntity itemEntity = toOrderItemEntity(item);
      orderEntity.addItem(itemEntity);
    });
    return orderEntity;
  }

  private OrderItem toOrderItemDomain(OrderItemEntity entity) {
    return new OrderItem()
      .id(entity.getId())
      .productId(entity.getProductId())
      .productName(entity.getProductName())
      .quantity(entity.getQuantity())
      .finalPrice(entity.getFinalPrice())
      .addedAt(entity.getAddedAt());
  }

  private OrderItemEntity toOrderItemEntity(OrderItem item) {
    return new OrderItemEntity()
      .id(item.getId())
      .productId(item.getProductId())
      .productName(item.getProductName())
      .quantity(item.getQuantity())
      .finalPrice(item.getFinalPrice())
      .addedAt(item.getAddedAt());
  }
}
