package com.felipe.ecommerce_cart_service.core.application.usecases;

import com.felipe.ecommerce_cart_service.core.domain.CartItem;

import java.util.List;

public interface GetAllCartItemsUseCase {
  List<CartItem> execute(String customerEmail);
}
