package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.domain.CartItem;

import java.util.UUID;

public interface AddItemToCartUseCase {
  CartItem execute(UUID productId, int quantity, String customerEmail);
}
