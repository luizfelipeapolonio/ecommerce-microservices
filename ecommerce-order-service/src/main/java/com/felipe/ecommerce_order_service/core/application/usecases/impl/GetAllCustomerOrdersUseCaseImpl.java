package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.GetAllCustomerOrdersUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.List;
import java.util.UUID;

public class GetAllCustomerOrdersUseCaseImpl implements GetAllCustomerOrdersUseCase {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;

  public GetAllCustomerOrdersUseCaseImpl(OrderGateway orderGateway, CustomerGateway customerGateway) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public List<Order> execute(String customerEmail) {
    CustomerProfileDTO authCustomer = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    UUID customerId = UUID.fromString(authCustomer.id());
    return this.orderGateway.getAllCustomerOrders(customerId);
  }
}
