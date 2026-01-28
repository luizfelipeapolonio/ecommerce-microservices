package com.felipe.ecommerce_cart_service.core.application.exceptions;

public class CartItemNotFoundException extends RuntimeException {
  public CartItemNotFoundException(Long itemId) {
    super("Item de id '" + itemId  + "' não encontrado no carrinho");
  }
}
