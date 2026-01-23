package com.felipe.ecommerce_cart_service.core.application.exceptions;

public class CartItemAlreadyExistsException extends RuntimeException {
  public CartItemAlreadyExistsException(String message) {
    super(message);
  }
}
