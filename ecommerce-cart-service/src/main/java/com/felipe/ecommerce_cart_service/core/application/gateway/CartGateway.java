package com.felipe.ecommerce_cart_service.core.application.gateway;

import com.felipe.ecommerce_cart_service.core.domain.Cart;

import java.util.UUID;

public interface CartGateway {
  Cart createCart(UUID customerId);
}
