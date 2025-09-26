package com.felipe.ecommerce_discount_service.infrastructure.exceptions;

public class CreatePromotionDTOValidationException extends RuntimeException {
  private final String field;
  private final String rejectedValue;
  private final String message;

  public CreatePromotionDTOValidationException(String field, String rejectedValue, String message) {
    super();
    this.field = field;
    this.rejectedValue = rejectedValue;
    this.message = message;
  }

  public String getField() {
    return this.field;
  }

  public String getRejectedValue() {
    return this.rejectedValue;
  }

  public String getMessage() {
    return this.message;
  }
}
