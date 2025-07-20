package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class CategoryAlreadyExistsException extends RuntimeException {
  public CategoryAlreadyExistsException(String categoryName) {
    super("A categoria '" + categoryName + "' já existe");
  }
}
