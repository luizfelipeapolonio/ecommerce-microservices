package com.felipe.ecommerce_upload_service.exceptions;

public class UnprocessableJsonException extends RuntimeException {
  public UnprocessableJsonException(String message, Throwable cause) {
    super(message, cause);
  }
}
