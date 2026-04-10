package com.felipe.ecommerce_order_service.infrastructure.exceptions;

public class CouponServiceException extends RuntimeException {
  public CouponServiceException(String message) {
    super(message);
  }
}
