package com.felipe.ecommerce_customer_service.core.application.usecases.impl;

import com.felipe.ecommerce_customer_service.core.application.usecases.CreateCustomerUseCase;
import com.felipe.ecommerce_customer_service.core.domain.Customer;
import com.felipe.ecommerce_customer_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_customer_service.core.application.gateway.AuthServerGateway;
import com.felipe.ecommerce_customer_service.core.application.dtos.CustomerAuthDataDTO;
import com.felipe.ecommerce_customer_service.core.application.exceptions.EmailAlreadyExistsException;

import java.util.Optional;

public class CreateCustomerUseCaseImpl implements CreateCustomerUseCase {
  private final CustomerGateway customerGateway;
  private final AuthServerGateway authServerGateway;

  public CreateCustomerUseCaseImpl(CustomerGateway customerGateway, AuthServerGateway authServerGateway) {
    this.customerGateway = customerGateway;
    this.authServerGateway = authServerGateway;
  }

  @Override
  public Customer execute(Customer customer, String password) {
    Optional<Customer> existingCustomer = this.customerGateway.findByEmail(customer.getEmail());
    if(existingCustomer.isPresent()) {
      throw new EmailAlreadyExistsException(customer.getEmail());
    }

    this.authServerGateway.registerCustomer(new CustomerAuthDataDTO(customer.getEmail(), password));
    return this.customerGateway.createCustomer(customer);
  }
}
