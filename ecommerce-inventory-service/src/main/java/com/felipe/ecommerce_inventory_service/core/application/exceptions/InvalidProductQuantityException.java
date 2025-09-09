package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class InvalidProductQuantityException extends RuntimeException {
  public InvalidProductQuantityException(String message) {
    super(message);
  }
}
