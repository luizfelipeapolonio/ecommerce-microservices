package com.felipe.ecommerce_order_service.infrastructure.exceptions;

import java.util.UUID;

public class SagaNotFoundException extends RuntimeException {
  public SagaNotFoundException(UUID sagaId) {
    super("Saga with id '" + sagaId + "' not found");
  }
}
