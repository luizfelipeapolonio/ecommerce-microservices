package com.felipe.ecommerce_discount_service.core.application.exceptions;

public class InvalidEndDateException extends RuntimeException {
  public InvalidEndDateException(String invalidEndDate) {
    super(
      "Data de término inválida! " +
      "A data de término não deve ser antes da data atual. Data inválida: " + invalidEndDate
    );
  }
}
