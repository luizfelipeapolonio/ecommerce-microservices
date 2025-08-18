package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
  public ProductAlreadyExistsException(String productName) {
    super("O produto '" + productName + "' já existe");
  }
}
