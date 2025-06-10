package com.felipe.ecommerce_customer_service.core.application.usecases.impl;

import com.felipe.ecommerce_customer_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.usecases.UpdateAddressUseCase;
import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;

public class UpdateAddressUseCaseImpl implements UpdateAddressUseCase {
  private final CustomerGateway customerGateway;

  public UpdateAddressUseCaseImpl(CustomerGateway customerGateway) {
    this.customerGateway = customerGateway;
  }

  @Override
  public Customer execute(String email, Address address) {
    Customer customer = this.customerGateway.findByEmail(email)
      .orElseThrow(() -> new DataNotFoundException("Cliente de email '" + email + "' não encontrado"));

    return this.customerGateway.updateAddress(customer, address);
  }
}
