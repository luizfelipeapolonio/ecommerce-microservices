package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.domain.Cart;

import java.util.UUID;

public interface CreateCartUseCase {
  Cart execute(UUID customerId);
}
