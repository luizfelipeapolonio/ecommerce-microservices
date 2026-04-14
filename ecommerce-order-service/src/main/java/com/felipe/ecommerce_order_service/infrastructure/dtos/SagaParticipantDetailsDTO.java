package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSagaParticipant;

public record SagaParticipantDetailsDTO(String participant, String status, String details) {
  public SagaParticipantDetailsDTO(OrderSagaParticipant sagaParticipant) {
    this(sagaParticipant.getName().toString(), sagaParticipant.getStatus().toString(), sagaParticipant.getDetails());
  }
}
