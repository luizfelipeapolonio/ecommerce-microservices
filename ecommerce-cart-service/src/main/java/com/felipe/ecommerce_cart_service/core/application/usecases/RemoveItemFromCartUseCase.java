package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.domain.CartItem;

public interface RemoveItemFromCartUseCase {
  CartItem execute(Long itemId, String customerEmail);
}
