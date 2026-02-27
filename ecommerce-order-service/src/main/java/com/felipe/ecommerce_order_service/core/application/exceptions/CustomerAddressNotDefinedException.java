package com.felipe.ecommerce_order_service.core.application.exceptions;

public class CustomerAddressNotDefinedException extends RuntimeException {
  public CustomerAddressNotDefinedException(String customerId) {
    super("Cliente de id '" + customerId + "' não possui um endereço definido.");
  }
}
