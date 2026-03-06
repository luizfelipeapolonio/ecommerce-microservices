package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.UUID;

public class GetOrderByIdUseCaseImpl implements GetOrderByIdUseCase {
  private final OrderGateway orderGateway;

  public GetOrderByIdUseCaseImpl(OrderGateway orderGateway) {
    this.orderGateway = orderGateway;
  }

  @Override
  public Order execute(UUID orderId) {
    return this.orderGateway.findOrderById(orderId)
      .orElseThrow(() -> new OrderNotFoundException(orderId));
  }
}
