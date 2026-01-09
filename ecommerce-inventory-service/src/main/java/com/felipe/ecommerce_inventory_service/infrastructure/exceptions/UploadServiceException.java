package com.felipe.ecommerce_inventory_service.infrastructure.exceptions;

public class UploadServiceException extends RuntimeException {
  public UploadServiceException(String message) {
    super(message);
  }
}
