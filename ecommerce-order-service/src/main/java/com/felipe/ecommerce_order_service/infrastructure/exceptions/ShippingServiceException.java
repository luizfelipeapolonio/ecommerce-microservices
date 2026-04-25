package com.felipe.ecommerce_order_service.infrastructure.exceptions;

public class ShippingServiceException extends RuntimeException {
  public ShippingServiceException(String message) {
    super(message);
  }
}
