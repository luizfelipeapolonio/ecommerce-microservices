package com.felipe.ecommerce_order_service.core.application.dtos;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;

public record UpdateOrderDTO(
  OrderStatus status,
  String productName,
  String checkoutUrl,
  String invoiceUrl,
  String finalPrice
) {}
