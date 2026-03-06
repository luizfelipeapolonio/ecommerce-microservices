package com.felipe.ecommerce_payment_service.infrastructure.exceptions;

public class UnsuccessfulTransactionException extends RuntimeException {
  public UnsuccessfulTransactionException(String message, Throwable cause) {
    super(message, cause);
  }
}
