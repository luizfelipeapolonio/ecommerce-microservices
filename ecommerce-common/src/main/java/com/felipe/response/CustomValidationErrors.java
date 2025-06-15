package com.felipe.response;

import org.springframework.validation.FieldError;

public class CustomValidationErrors {
  private String field;
  private Object rejectedValue;
  private String cause;

  public CustomValidationErrors() {}

  public CustomValidationErrors(FieldError fieldError) {
    this.field = fieldError.getField();
    this.rejectedValue = checkForPasswordFieldRejectedValue(fieldError.getField(), fieldError.getRejectedValue());
    this.cause = fieldError.getDefaultMessage();
  }

  public String getField() {
    return this.field;
  }

  public void setField(String field) {
    this.field = field;
  }

  public Object getRejectedValue() {
    return this.rejectedValue;
  }

  public void setRejectedValue(Object rejectedValue) {
    this.rejectedValue = rejectedValue;
  }

  public String getCause() {
    return this.cause;
  }

  public void setCause(String cause) {
    this.cause = cause;
  }

  private Object checkForPasswordFieldRejectedValue(String field, Object rejectedValue) {
    return field.equalsIgnoreCase("password") ? "" : rejectedValue;
  }
}
