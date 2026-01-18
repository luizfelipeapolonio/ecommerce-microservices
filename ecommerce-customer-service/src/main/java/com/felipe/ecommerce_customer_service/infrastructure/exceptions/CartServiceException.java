package com.felipe.ecommerce_customer_service.infrastructure.exceptions;

public class CartServiceException extends RuntimeException {
  public CartServiceException(String message) {
    super(message);
  }
}
