package com.felipe.ecommerce_customer_service.core.application.usecases.impl;

import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.GetCustomerByEmailUseCase;
import com.felipe.ecommerce_customer_service.core.domain.Customer;

public class GetCustomerByEmailUseCaseImpl implements GetCustomerByEmailUseCase {
  private final CustomerGateway customerGateway;

  public GetCustomerByEmailUseCaseImpl(CustomerGateway customerGateway) {
    this.customerGateway = customerGateway;
  }

  @Override
  public Customer execute(String email) {
    return this.customerGateway.findByEmail(email)
      .orElseThrow(() -> new DataNotFoundException("Cliente de email '" + email + "' não encontrado"));
  }
}
