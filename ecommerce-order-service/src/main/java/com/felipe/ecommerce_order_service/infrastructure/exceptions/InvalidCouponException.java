package com.felipe.ecommerce_order_service.infrastructure.exceptions;

public class InvalidCouponException extends RuntimeException {
  public InvalidCouponException(String message) {
    super(message);
  }
}
