package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class MinimumPriceException extends RuntimeException {
  public MinimumPriceException(String message) {
    super(message);
  }
}
