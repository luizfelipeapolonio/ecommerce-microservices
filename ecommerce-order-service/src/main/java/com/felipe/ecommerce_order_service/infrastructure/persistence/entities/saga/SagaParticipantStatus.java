package com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga;

public enum SagaParticipantStatus {
  SUCCESS,
  PROCESSING,
  PENDING,
  FAILURE
}
