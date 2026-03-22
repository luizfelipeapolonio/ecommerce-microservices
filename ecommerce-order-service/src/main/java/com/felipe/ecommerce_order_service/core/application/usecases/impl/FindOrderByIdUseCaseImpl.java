package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.FindOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.Optional;
import java.util.UUID;

public class FindOrderByIdUseCaseImpl implements FindOrderByIdUseCase {
  private final OrderGateway orderGateway;

  public FindOrderByIdUseCaseImpl(OrderGateway orderGateway) {
    this.orderGateway = orderGateway;
  }

  @Override
  public Optional<Order> execute(UUID orderId) {
    return this.orderGateway.findOrderById(orderId);
  }
}
