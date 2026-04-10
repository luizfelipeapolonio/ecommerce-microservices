package com.felipe.ecommerce_order_service.core.application.usecases.impl;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;
import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_order_service.core.application.exceptions.CustomerAddressNotDefinedException;
import com.felipe.ecommerce_order_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_order_service.core.application.gateway.OrderGateway;
import com.felipe.ecommerce_order_service.core.application.usecases.CreateOrderUseCase;

import java.util.Map;
import java.util.UUID;

public class CreateOrderUseCaseImpl implements CreateOrderUseCase {
  private final OrderGateway orderGateway;
  private final CustomerGateway customerGateway;
  private final CouponGateway couponGateway;

  public CreateOrderUseCaseImpl(OrderGateway orderGateway, CustomerGateway customerGateway, CouponGateway couponGateway) {
    this.orderGateway = orderGateway;
    this.customerGateway = customerGateway;
    this.couponGateway = couponGateway;
  }

  @Override
  public Map<String, UUID> execute(CreateOrderDTO orderDTO, String customerEmail) {
    // Retrieve authenticated user profile
    CustomerProfileDTO customerProfile = this.customerGateway.fetchAuthCustomerProfile(customerEmail);
    if (customerProfile.address() == null) {
      throw new CustomerAddressNotDefinedException(customerProfile.id());
    }
    if (orderDTO.couponCode() != null) {
      this.couponGateway.checkIfCouponIsValid(orderDTO.couponCode());
    }
    UUID customerId = UUID.fromString(customerProfile.id());
    return this.orderGateway.createOrder(customerId, orderDTO);
  }
}
