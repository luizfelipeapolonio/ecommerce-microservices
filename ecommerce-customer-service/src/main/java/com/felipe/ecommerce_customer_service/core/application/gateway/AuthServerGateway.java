package com.felipe.ecommerce_customer_service.core.application.gateway;

import com.felipe.ecommerce_customer_service.core.application.dtos.CustomerAuthDataDTO;
import com.felipe.ecommerce_customer_service.core.application.exceptions.AuthServerException;

public interface AuthServerGateway {
  void registerCustomer(CustomerAuthDataDTO customerData) throws AuthServerException;
}
