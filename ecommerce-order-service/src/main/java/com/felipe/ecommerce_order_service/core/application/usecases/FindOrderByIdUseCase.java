package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.Optional;
import java.util.UUID;

public interface FindOrderByIdUseCase {
  Optional<Order> execute(UUID orderId);
}
