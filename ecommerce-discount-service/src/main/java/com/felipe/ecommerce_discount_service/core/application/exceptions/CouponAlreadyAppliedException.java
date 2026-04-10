package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class CouponAlreadyAppliedException extends RuntimeException {
  public CouponAlreadyAppliedException(String couponCode) {
    super("O cupom '" + couponCode + "' já foi aplicado");
  }
}
