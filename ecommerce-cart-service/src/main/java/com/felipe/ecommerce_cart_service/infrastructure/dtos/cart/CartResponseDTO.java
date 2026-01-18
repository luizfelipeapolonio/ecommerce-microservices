package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import com.felipe.ecommerce_cart_service.core.domain.Cart;

public record CartResponseDTO(String id, String customerId, String createdAt) {
  public CartResponseDTO(Cart cart) {
    this(cart.getId().toString(), cart.getCustomerId().toString(), cart.getCreatedAt().toString());
  }
}
