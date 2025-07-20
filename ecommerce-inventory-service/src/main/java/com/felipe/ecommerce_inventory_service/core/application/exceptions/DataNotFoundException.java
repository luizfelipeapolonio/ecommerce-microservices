package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class DataNotFoundException extends RuntimeException {
  public DataNotFoundException(String message) {
    super(message);
  }
  public DataNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
