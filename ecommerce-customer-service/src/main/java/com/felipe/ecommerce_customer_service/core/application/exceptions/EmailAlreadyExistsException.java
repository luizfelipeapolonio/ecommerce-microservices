package com.felipe.ecommerce_customer_service.core.application.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException(String email) {
    super("E-mail '" + email + "' já cadastrado");
  }
}
