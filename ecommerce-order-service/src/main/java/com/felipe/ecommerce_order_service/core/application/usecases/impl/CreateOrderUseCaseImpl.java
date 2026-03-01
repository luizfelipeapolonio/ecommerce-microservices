package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.CustomerAddressNotDefinedException;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;

import java.util.Map;
import java.util.UUID;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;

  public CreateOrderUseCaseImpl(OrderGateway orderGateway, CustomerGateway customerGateway) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public Map<String, UUID> execute(CreateOrderDTO orderDTO, String customerEmail) {
    // Retrieve authenticated user profile
    CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    if (customerProfile.address() == null) {
      throw new CustomerAddressNotDefinedException(customerProfile.id());
    }
    UUID customerId = UUID.fromString(customerProfile.id());
    return this.orderGateway.createOrder(customerId, orderDTO.productId(), orderDTO.productQuantity());
  }
}
