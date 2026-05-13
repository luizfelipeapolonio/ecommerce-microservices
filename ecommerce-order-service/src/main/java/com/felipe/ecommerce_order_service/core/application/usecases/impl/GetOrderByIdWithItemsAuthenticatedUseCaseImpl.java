package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.OrderNotFoundException;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.GetOrderByIdWithItemsUseCase;
import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.UUID;

public class GetOrderByIdWithItemsAuthenticatedUseCaseImpl implements GetOrderByIdWithItemsUseCase {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;

  public GetOrderByIdWithItemsAuthenticatedUseCaseImpl(OrderGateway orderGateway, CustomerGateway customerGateway) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
  }

  @Override
  public Order execute(UUID orderId, String customerEmail) {
    CustomerProfileDTO authCustomer = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    UUID customerId = UUID.fromString(authCustomer.id());
    return this.orderGateway.findOrderByIdAndCustomerIdWithItems(orderId, customerId)
      .orElseThrow(() -> new OrderNotFoundException("Pedido de id '" + orderId + "' do usuário de e-mail '" + customerEmail + "' não encontrado"));
  }
}
