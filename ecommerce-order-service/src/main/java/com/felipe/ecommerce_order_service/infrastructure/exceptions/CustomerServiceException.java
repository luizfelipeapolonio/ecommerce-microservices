package com.felipe.ecommerce_order_service.infrastructure.exceptions;

public class CustomerServiceException extends RuntimeException {
  public CustomerServiceException(String message) {
    super(message);
  }
}
