package com.felipe.ecommerce_customer_service.core.application.usecases.impl;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.UpdateCustomerProfileUseCase;
import com.felipe.ecommerce_customer_service.core.domain.Customer;

public class UpdateCustomerProfileUseCaseImpl implements UpdateCustomerProfileUseCase {
  private final CustomerGateway customerGateway;

  public UpdateCustomerProfileUseCaseImpl(CustomerGateway customerGateway) {
    this.customerGateway = customerGateway;
  }

  @Override
  public Customer execute(String email, UpdateCustomerDTO customerDTO) {
    Customer customer = this.customerGateway.findByEmail(email)
      .orElseThrow(() -> new DataNotFoundException("Cliente de email '" + email + "' não encontrado"));

    return this.customerGateway.updateCustomer(customer, customerDTO);
  }
}
