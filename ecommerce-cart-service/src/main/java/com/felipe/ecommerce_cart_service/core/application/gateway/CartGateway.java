package com.felipe.ecommerce_cart_service.core.application.gateway;

import com.felipe.ecommerce_cart_service.core.application.dtos.CartItemDTO;
import com.felipe.ecommerce_cart_service.core.domain.Cart;

import java.util.Optional;
import java.util.UUID;

public interface CartGateway {
  Cart createCart(UUID customerId);
  CartItemDTO addItemToCart(Cart cart, UUID productId, int quantity);
  void removeItemFromCart(Cart cart);
  void updateCartItem(Cart cart);
  Optional<Cart> findCartByCustomerId(UUID customerId);
}
