package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.math.BigDecimal;
import java.util.UUID;

public class UpdateOrderUseCaseImpl implements UpdateOrderUseCase {
  private final OrderGateway orderGateway;

  public UpdateOrderUseCaseImpl(OrderGateway orderGateway) {
    this.orderGateway = orderGateway;
  }

  @Override
  public Order execute(UUID orderId, UpdateOrderDTO orderDTO) {
    Order updatedOrder = this.orderGateway.findOrderById(orderId)
      .map(order -> {
        if (orderDTO.status() != null) {
          order.setStatus(orderDTO.status());
        }
        if (orderDTO.productName() != null) {
          order.setProductName(orderDTO.productName());
        }
        if (orderDTO.checkoutUrl() != null) {
          order.setCheckoutUrl(orderDTO.checkoutUrl());
        }
        if (orderDTO.invoiceUrl() != null) {
          order.setInvoiceUrl(orderDTO.invoiceUrl());
        }
        if (orderDTO.finalPrice() != null) {
          order.setFinalPrice(new BigDecimal(orderDTO.finalPrice()));
        }
        return order;
      })
      .orElseThrow(() -> new OrderNotFoundException(orderId));
    return this.orderGateway.updateOrder(updatedOrder);
  }
}
