package com.felipe.ecommerce_inventory_service.core.application.exceptions;

public class ReservationAlreadyExistsException extends RuntimeException {
  public ReservationAlreadyExistsException(String message) {
    super(message);
  }
}
