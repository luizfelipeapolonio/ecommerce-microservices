package com.felipe.ecommerce_order_service.core.application.exceptions;

import java.util.UUID;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(UUID orderId) {
    super("Pedido de id '" + orderId + "' não encontrado");
  }
}
