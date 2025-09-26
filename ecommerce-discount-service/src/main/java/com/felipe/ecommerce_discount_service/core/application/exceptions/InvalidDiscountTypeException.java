package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class InvalidDiscountTypeException extends RuntimeException {
  public InvalidDiscountTypeException(String value) {
    super("Tipo de desconto inválido! O valor '" + value + "' não é um tipo válido de desconto");
  }
}
