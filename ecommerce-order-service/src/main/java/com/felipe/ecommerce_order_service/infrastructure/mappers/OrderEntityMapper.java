package com.felipe.ecommerce_order_service.infrastructure.mappers;

import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderEntityMapper {
  public Order toDomain(OrderEntity orderEntity) {
    return new Order()
      .id(orderEntity.getId())
      .customerId(orderEntity.getCustomerId())
      .productId(orderEntity.getProductId())
      .productName(orderEntity.getProductName())
      .productQuantity(orderEntity.getProductQuantity())
      .finalPrice(orderEntity.getFinalPrice())
      .status(OrderStatus.of(orderEntity.getStatus()))
      .withCoupon(orderEntity.isWithCoupon())
      .couponId(orderEntity.getCouponId())
      .checkoutUrl(orderEntity.getCheckoutUrl())
      .invoiceUrl(orderEntity.getInvoiceUrl())
      .createdAt(orderEntity.getCreatedAt())
      .updatedAt(orderEntity.getUpdatedAt());
  }

  public OrderEntity toEntity(Order order) {
    return new OrderEntity()
      .id(order.getId())
      .customerId(order.getCustomerId())
      .productId(order.getProductId())
      .productName(order.getProductName())
      .productQuantity(order.getProductQuantity())
      .finalPrice(order.getFinalPrice())
      .status(OrderStatus.of(order.getStatus()))
      .withCoupon(order.isWithCoupon())
      .couponId(order.getCouponId())
      .checkoutUrl(order.getCheckoutUrl())
      .invoiceUrl(order.getInvoiceUrl())
      .createdAt(order.getCreatedAt())
      .updatedAt(order.getUpdatedAt());
  }
}
