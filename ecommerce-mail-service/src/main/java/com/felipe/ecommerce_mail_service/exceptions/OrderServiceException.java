package com.felipe.ecommerce_mail_service.exceptions;

public class OrderServiceException extends RuntimeException {
  public OrderServiceException(String message) {
    super(message);
  }
}
