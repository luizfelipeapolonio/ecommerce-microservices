package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.domain.CartItem;

public interface UpdateCartItemUseCase {
  CartItem execute(Long itemId, int quantity, String customerEmail);
}
