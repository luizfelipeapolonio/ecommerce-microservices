package com.felipe.ecommerce_payment_service.infrastructure.exceptions;

public class InsufficientCustomerBalanceException extends RuntimeException {
  public InsufficientCustomerBalanceException(String message) {
    super(message);
  }
}
