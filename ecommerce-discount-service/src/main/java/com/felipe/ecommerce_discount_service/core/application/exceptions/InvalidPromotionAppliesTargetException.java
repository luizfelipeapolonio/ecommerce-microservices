package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class InvalidPromotionAppliesTargetException extends RuntimeException {
  public InvalidPromotionAppliesTargetException(String value) {
    super("Não é possível aplicar promoção para '" + value + "' pois não é um valor válido");
  }
}
