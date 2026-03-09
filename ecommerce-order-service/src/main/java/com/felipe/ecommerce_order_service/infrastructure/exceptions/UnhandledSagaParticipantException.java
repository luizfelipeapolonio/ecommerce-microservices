package com.felipe.ecommerce_order_service.infrastructure.exceptions;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;

public class UnhandledSagaParticipantException extends RuntimeException {
  public UnhandledSagaParticipantException(String participantName, SagaStatus status) {
    super("Unhandled saga participant: '" + participantName + "' in state '" + status.name() + "'");
  }
}
