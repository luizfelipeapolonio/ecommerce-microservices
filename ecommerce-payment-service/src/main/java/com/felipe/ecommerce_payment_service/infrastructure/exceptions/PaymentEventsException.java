package com.felipe.ecommerce_payment_service.infrastructure.exceptions;

public class PaymentEventsException extends RuntimeException {
  public PaymentEventsException(String message) {
    super(message);
  }
}
