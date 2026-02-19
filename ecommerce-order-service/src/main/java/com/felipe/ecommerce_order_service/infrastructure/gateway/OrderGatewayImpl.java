package com.felipe.ecommerce_order_service.infrastructure.gateway;

import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.domain.Order;
import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import com.felipe.ecommerce_order_service.infrastructure.mappers.OrderEntityMapper;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.OrderEntity;
import com.felipe.ecommerce_order_service.infrastructure.persistence.repositories.OrderRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderGatewayImpl implements OrderGateway {
  private final OrderRepository orderRepository;
  private final OrderEntityMapper orderEntityMapper;

  public OrderGatewayImpl(OrderRepository orderRepository, OrderEntityMapper orderEntityMapper) {
    this.orderRepository = orderRepository;
    this.orderEntityMapper = orderEntityMapper;
  }

  @Override
  public Order createOrder(UUID customerId, UUID productId, int productQuantity) {
    OrderEntity newOrder = new OrderEntity()
      .customerId(customerId)
      .productId(productId)
      .productQuantity(productQuantity)
      .status(OrderStatus.PENDING)
      .productName("PENDING")
      .finalPrice(new BigDecimal("0.00"));
    OrderEntity savedOrder = this.orderRepository.save(newOrder);
    return this.orderEntityMapper.toDomain(savedOrder);
  }

  @Override
  public Optional<Order> findOrderById(UUID orderId) {
    return this.orderRepository.findById(orderId).map(this.orderEntityMapper::toDomain);
  }

  @Override
  public void deleteOrder(UUID orderId) {
    this.orderRepository.deleteById(orderId);
  }
}
