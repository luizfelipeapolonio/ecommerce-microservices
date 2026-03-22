package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.infrastructure.persistence.entities.saga.SagaStatus;

import java.util.UUID;

public record OrderStatusDTO(UUID orderId, SagaStatus status, String failureReason, String checkoutUrl, String invoiceUrl) {
  public OrderStatusDTO {
    checkoutUrl = checkoutUrl == null ? "NOT_AVAILABLE" : checkoutUrl;
    invoiceUrl = invoiceUrl == null ? "NOT_AVAILABLE" : invoiceUrl;
  }
}
