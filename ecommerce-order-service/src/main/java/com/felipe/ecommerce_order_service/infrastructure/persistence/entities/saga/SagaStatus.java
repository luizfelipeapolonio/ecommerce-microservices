package com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga;

public enum SagaStatus {
  STARTED,
  PROCESSING,
  CANCELLING,
  FAILED,
  COMPLETED
}
