package com.felipe.ecommerce_order_service.core.application.dtos;

import java.util.UUID;

public interface CreateOrderDTO {
  UUID productId();
  int productQuantity();
  String couponCode();
}
