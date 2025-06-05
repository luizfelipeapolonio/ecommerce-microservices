package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.domain.Customer;

public interface CreateCustomerUseCase {
  Customer execute(Customer customer, String password);
}
