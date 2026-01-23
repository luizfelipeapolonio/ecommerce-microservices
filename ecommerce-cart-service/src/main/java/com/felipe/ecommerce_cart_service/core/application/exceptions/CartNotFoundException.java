package com.felipe.ecommerce_cart_service.core.application.exceptions;

public class CartNotFoundException extends RuntimeException {
  public CartNotFoundException(String message) {
    super(message);
  }
}
