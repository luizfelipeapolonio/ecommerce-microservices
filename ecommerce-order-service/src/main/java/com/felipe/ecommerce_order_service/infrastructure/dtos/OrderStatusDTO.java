package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.OrderSaga;
import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;

import java.util.List;
import java.util.UUID;

public record OrderStatusDTO(
  UUID orderId,
  SagaStatus status,
  String failureReason,
  String checkoutUrl,
  String invoiceUrl,
  List<SagaParticipantDetailsDTO> participantDetails
) {
  public OrderStatusDTO {
    checkoutUrl = checkoutUrl == null ? "NOT_AVAILABLE" : checkoutUrl;
    invoiceUrl = invoiceUrl == null ? "NOT_AVAILABLE" : invoiceUrl;
  }

  public OrderStatusDTO(UUID orderId, OrderSaga saga, String checkoutUrl, String invoiceUrl, boolean withDetails) {
    this(
      orderId,
      saga.getStatus(),
      saga.getFailureReason(),
      checkoutUrl,
      invoiceUrl,
      withDetails
        ? saga.getParticipants().stream().map(SagaParticipantDetailsDTO::new).toList()
        : null
    );
  }
}
