package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class BrandAlreadyExistsException extends RuntimeException {
  public BrandAlreadyExistsException(String name) {
    super("A marca '" + name + "' já existe");
  }
}
