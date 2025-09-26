package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class InvalidPromotionScopeException extends RuntimeException {
  public InvalidPromotionScopeException(String value) {
    super("Escopo de promoção inválido! O valor '" + value + "' não é um escopo de promoção válido");
  }
}
