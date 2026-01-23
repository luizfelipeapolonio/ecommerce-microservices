package com.felipe.ecommerce_cart_service.infrastructure.exceptions;

public class CustomerServiceException extends RuntimeException {
  public CustomerServiceException(String message) {
    super(message);
  }
}
