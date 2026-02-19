package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.DeleteOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.UUID;

public class DeleteOrderUseCaseImpl implements DeleteOrderUseCase {
  private final OrderGateway orderGateway;

  public DeleteOrderUseCaseImpl(OrderGateway orderGateway) {
    this.orderGateway = orderGateway;
  }

  @Override
  public Order execute(UUID orderId) {
    Order order = this.orderGateway.findOrderById(orderId)
      .orElseThrow(() -> new OrderNotFoundException(orderId));
    this.orderGateway.deleteOrder(orderId);
    return order;
  }
}
