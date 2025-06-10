package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.application.dtos.UpdateCustomerDTO;
import com.felipe.ecommerce_customer_service.core.domain.Customer;

public interface UpdateCustomerProfileUseCase {
  Customer execute(String email, UpdateCustomerDTO customerDTO);
}
