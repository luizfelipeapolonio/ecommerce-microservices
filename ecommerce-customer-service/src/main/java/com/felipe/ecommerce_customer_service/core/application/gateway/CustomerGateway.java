package com.felipe.ecommerce_customer_service.core.application.gateway;

import com.felipe.ecommerce_customer_service.core.domain.Customer;

import java.util.Optional;

public interface CustomerGateway {
  Customer createCustomer(Customer customer);
  Optional<Customer> findByEmail(String email);
}
