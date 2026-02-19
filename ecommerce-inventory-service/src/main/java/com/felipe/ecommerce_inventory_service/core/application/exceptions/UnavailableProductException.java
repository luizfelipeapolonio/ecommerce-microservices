package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class UnavailableProductException extends RuntimeException {
  public UnavailableProductException(String message) {
    super(message);
  }
}
