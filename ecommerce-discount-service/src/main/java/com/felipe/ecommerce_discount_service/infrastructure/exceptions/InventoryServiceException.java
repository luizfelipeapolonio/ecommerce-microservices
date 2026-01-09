package com.felipe.ecommerce_discount_service.infrastructure.exceptions;

public class InventoryServiceException extends RuntimeException {
  public InventoryServiceException(String message) {
    super(message);
  }
}
