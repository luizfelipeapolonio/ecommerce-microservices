package com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga;

public enum SagaStatus {
  STARTED,
  PROCESSING,
  WAITING_FOR_PAYMENT,
  COMMITING,
  CANCELLING,
  FAILED,
  COMPLETED
}
