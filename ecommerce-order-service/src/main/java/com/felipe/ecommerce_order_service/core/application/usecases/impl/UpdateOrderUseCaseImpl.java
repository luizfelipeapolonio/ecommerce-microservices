package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.UpdateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.UpdateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.OrderItem;

import java.math.BigDecimal;
import java.util.Map;
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
        if (!orderDTO.products().isEmpty()) {
          Map<UUID, OrderItem> items = order.getItemsMap();
          orderDTO.products().forEach(productDTO -> {
            OrderItem item = items.get(productDTO.id());
            if (productDTO.name() != null) {
              item.setProductName(productDTO.name());
            }
            if (productDTO.unitPrice() != null) {
              item.setFinalPrice(productDTO.unitPrice());
            }
          });
        }
        if (orderDTO.status() != null) {
          order.setStatus(orderDTO.status());
        }
        if (orderDTO.checkoutUrl() != null) {
          order.setCheckoutUrl(orderDTO.checkoutUrl());
        }
        if (orderDTO.invoiceUrl() != null) {
          order.setInvoiceUrl(orderDTO.invoiceUrl());
        }
        if (orderDTO.orderPrice() != null) {
          order.setOrderPrice(new BigDecimal(orderDTO.orderPrice()));
        }
        if (orderDTO.withCoupon() != null) {
          if (!orderDTO.withCoupon()) {
            order.setWithCoupon(false);
            order.setCouponCode(null);
          }
        }
        return order;
      })
      .orElseThrow(() -> new OrderNotFoundException(orderId));
    return this.orderGateway.updateOrder(updatedOrder);
  }
}
