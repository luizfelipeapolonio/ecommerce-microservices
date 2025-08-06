package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class ModelAlreadyExistsException extends RuntimeException {
  public ModelAlreadyExistsException(String name) {
    super("O modelo '" + name + "' já existe");
  }
}
