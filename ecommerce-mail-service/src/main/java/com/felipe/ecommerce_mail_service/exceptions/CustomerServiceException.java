package com.felipe.ecommerce_mail_service.exceptions;

public class CustomerServiceException extends RuntimeException {
  public CustomerServiceException(String message) {
    super(message);
  }
}
