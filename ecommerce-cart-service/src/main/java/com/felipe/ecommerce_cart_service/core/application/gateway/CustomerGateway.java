package com.felipe.ecommerce_cart_service.core.application.gateway;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;

public interface CustomerGateway {
  CustomerProfileDTO fetchAuthCustomerProfile(String customerEmail);
}
