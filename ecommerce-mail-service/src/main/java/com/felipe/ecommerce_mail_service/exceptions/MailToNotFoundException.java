package com.felipe.ecommerce_mail_service.exceptions;

public class MailToNotFoundException extends RuntimeException {
  public MailToNotFoundException(String message) {
    super(message);
  }
}
