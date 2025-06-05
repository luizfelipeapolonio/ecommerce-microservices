package com.felipe.ecommerce_auth_server.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {
  public EmailAlreadyExistsException(String email) {
    super("E-mail '" + email + "' já cadastrado");
  }
}
