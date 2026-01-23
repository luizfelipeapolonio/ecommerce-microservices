package com.felipe.ecommerce_cart_service.core.application.gateway;

import com.felipe.ecommerce_cart_service.core.domain.Cart;
import com.felipe.ecommerce_cart_service.core.domain.CartItem;

import java.util.Optional;
import java.util.UUID;

public interface CartGateway {
  Cart createCart(UUID customerId);
  Optional<Cart> findCartByCustomerId(UUID customerId);
  CartItem addItemToCart(Cart cart, UUID productId, int quantity);
  Optional<CartItem> findCartItemByProductIdAndCartId(UUID productId, UUID cartId);
}
