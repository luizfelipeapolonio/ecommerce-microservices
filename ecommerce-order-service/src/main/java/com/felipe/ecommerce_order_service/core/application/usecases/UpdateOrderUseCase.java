package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.UUID;

public interface UpdateOrderUseCase {
  Order execute(UUID orderId, UpdateOrderDTO orderDTO);
}
