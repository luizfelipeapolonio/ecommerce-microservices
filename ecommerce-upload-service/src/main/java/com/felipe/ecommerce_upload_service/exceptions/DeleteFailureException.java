package com.felipe.ecommerce_upload_service.exceptions;

public class DeleteFailureException extends RuntimeException {
  public DeleteFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
