package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class DataNotFoundException extends RuntimeException {
  public DataNotFoundException(String message) {
    super(message);
  }
}
