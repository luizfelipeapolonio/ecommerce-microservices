package com.felipe.ecommerce_cart_service.core.application.usecases.impl;

import com.felipe.ecommerce_cart_service.core.application.gateway.CartGateway;
import com.felipe.ecommerce_cart_service.core.application.usecases.CreateCartUseCase;
import com.felipe.ecommerce_cart_service.core.domain.Cart;

import java.util.UUID;

public class CreateCartUseCaseImpl implements CreateCartUseCase {
  private final CartGateway cartGateway;

  public CreateCartUseCaseImpl(CartGateway cartGateway) {
    this.cartGateway = cartGateway;
  }

  @Override
  public Cart execute(UUID customerId) {
    return this.cartGateway.createCart(customerId);
  }
}
