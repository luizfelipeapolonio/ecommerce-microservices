package com.felipe.ecommerce_discount_service.infrastructure.exceptions;

public class InvalidRequestParamException extends RuntimeException {
  private final String paramName;
  private final Object rejectedValue;

  public InvalidRequestParamException(String paramName, Object rejectedValue, String message) {
    super(message);
    this.paramName = paramName;
    this.rejectedValue = rejectedValue;
  }

  public String getParamName() {
    return this.paramName;
  }

  public Object getRejectedValue() {
    return this.rejectedValue;
  }
}
