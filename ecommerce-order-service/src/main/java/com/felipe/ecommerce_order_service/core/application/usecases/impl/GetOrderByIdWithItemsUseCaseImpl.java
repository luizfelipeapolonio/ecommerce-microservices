package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdWithItemsUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.UUID;

public class GetOrderByIdWithItemsUseCaseImpl implements GetOrderByIdWithItemsUseCase {
  private final OrderGateway orderGateway;

  public GetOrderByIdWithItemsUseCaseImpl(OrderGateway orderGateway) {
    this.orderGateway = orderGateway;
  }

  @Override
  public Order execute(UUID orderId) {
    return this.orderGateway.findOrderByIdWithItems(orderId)
      .orElseThrow(() -> new OrderNotFoundException(orderId));
  }
}
