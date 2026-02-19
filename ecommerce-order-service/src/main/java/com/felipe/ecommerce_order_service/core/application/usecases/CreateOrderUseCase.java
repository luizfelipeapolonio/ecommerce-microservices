package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;

import java.util.Map;
import java.util.UUID;

public interface CreateOrderUseCase {
  Map<String, UUID> execute(CreateOrderDTO orderDTO, String customerEmail);
}
