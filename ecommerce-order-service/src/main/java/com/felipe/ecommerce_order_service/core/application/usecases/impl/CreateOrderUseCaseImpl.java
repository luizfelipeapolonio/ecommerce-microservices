package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderTransactionGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.Map;
import java.util.UUID;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;
  private final OrderTransactionGateway orderTransactionGateway;

  public CreateOrderUseCaseImpl(OrderGateway orderGateway, CustomerGateway customerGateway,
                                OrderTransactionGateway orderTransactionGateway) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
    this.orderTransactionGateway = orderTransactionGateway;
  }

  @Override
  public Map<String, UUID> execute(CreateOrderDTO orderDTO, String customerEmail) {
    // Retrieve authenticated user profile
    CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    UUID customerId = UUID.fromString(customerProfile.id());
    Order createdOrder = this.orderGateway.createOrder(customerId, orderDTO.productId(), orderDTO.productQuantity());
    UUID transactionId = this.orderTransactionGateway.executeOrderTransaction(createdOrder.getId(), orderDTO.productId(), orderDTO.productQuantity());
    return Map.of(
      "orderId", createdOrder.getId(),
      "sagaId", transactionId
    );
  }
}
