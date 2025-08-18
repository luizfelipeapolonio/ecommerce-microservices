package com.felipe.ecommerce_inventory_service.infrastructure.exceptions;

public class UnprocessableJsonException extends RuntimeException {
  public UnprocessableJsonException(String message, Throwable cause) {
    super(message, cause);
  }
}
