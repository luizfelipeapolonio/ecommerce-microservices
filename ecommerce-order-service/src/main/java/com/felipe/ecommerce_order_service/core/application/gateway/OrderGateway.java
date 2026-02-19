package com.felipe.ecommerce_order_service.core.application.gateway;

import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface OrderGateway {
  Order createOrder(UUID customerId, UUID productId, int productQuantity);
  Optional<Order> findOrderById(UUID orderId);
  void deleteOrder(UUID orderId);
}
