package com.felipe.ecommerce_payment_service.core.application.exceptions;

public class PaymentNotFoundException extends RuntimeException {
  public PaymentNotFoundException(String message) {
    super(message);
  }
}
