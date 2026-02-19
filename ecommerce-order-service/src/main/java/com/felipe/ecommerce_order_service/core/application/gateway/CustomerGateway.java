package com.felipe.ecommerce_order_service.core.application.gateway;

import com.felipe.ecommerce_order_service.core.application.dtos.CustomerProfileDTO;

public interface CustomerGateway {
  CustomerProfileDTO fetchAuthCustomerProfile(String customerEmail);
}
