package com.felipe.ecommerce_inventory_service.infrastructure.exceptions;

public class MappingFailureException extends RuntimeException {
  public MappingFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
