package com.felipe.ecommerce_order_service.core.exceptions;

public class InvalidOrderStatusException extends RuntimeException {
  public InvalidOrderStatusException(String value) {
    super("Tipo de desconto inválido! O valor '" + value + "' não é um tipo válido de status de pedido");
  }
}
