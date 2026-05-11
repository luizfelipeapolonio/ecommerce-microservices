package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;

import java.util.List;

public record OrderResponseDTO(String id,
                               String orderPrice,
                               boolean withCoupon,
                               String couponCode,
                               String couponValue,
                               String shippingFee,
                               String checkoutUrl,
                               String invoiceUrl,
                               String customerId,
                               String status,
                               String createdAt,
                               String updatedAt,
                               List<Item> orderItems) {
  public OrderResponseDTO(Order order) {
    this(
      order.getId().toString(),
      order.getOrderPrice().toPlainString(),
      order.isWithCoupon(),
      order.getCouponCode(),
      order.getCouponValue() == null ? null : order.getCouponValue().toPlainString(),
      order.getShippingFee().toPlainString(),
      order.getCheckoutUrl(),
      order.getInvoiceUrl(),
      order.getCustomerId().toString(),
      order.getStatus(),
      order.getCreatedAt().toString(),
      order.getUpdatedAt().toString(),
      order.getItems().stream().map(Item::new).toList()
    );
  }

  public record Item(long id, String productId, String productName, long quantity, String finalPrice, String addedAt) {
    public Item(OrderItem item) {
      this(
        item.getId(),
        item.getProductId().toString(),
        item.getProductName(),
        item.getQuantity(),
        item.getFinalPrice().toPlainString(),
        item.getAddedAt().toString()
      );
    }
  }
}
