package com.felipe.ecommerce_customer_service.core.application.usecases;

import com.felipe.ecommerce_customer_service.core.domain.Address;
import com.felipe.ecommerce_customer_service.core.domain.Customer;

public interface InsertAddressUseCase {
  Customer execute(String email, Address address);
}
