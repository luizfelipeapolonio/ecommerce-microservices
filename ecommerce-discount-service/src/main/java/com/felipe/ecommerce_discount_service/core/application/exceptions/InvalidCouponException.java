package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class InvalidCouponException extends RuntimeException {
  public InvalidCouponException(String couponCode) {
    super("O cupom '" + couponCode + "' é inválido");
  }
}
