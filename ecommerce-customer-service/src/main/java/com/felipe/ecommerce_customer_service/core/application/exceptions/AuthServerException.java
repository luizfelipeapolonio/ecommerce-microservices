package com.felipe.ecommerce_customer_service.core.application.exceptions;

public class AuthServerException extends RuntimeException {
  public AuthServerException(String message) {
    super(message);
  }

  public AuthServerException(String message, Throwable cause) {
    super(message, cause);
  }
}
