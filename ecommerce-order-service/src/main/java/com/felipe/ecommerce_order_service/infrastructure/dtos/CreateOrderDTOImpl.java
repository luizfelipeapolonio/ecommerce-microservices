package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;

import java.util.UUID;

public record CreateOrderDTOImpl(
  UUID productId,
  int productQuantity,
  String couponCode
) implements CreateOrderDTO {}
