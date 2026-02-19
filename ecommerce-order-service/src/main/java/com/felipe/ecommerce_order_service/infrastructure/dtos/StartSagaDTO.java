package com.felipe.ecommerce_order_service.infrastructure.dtos;

public record StartSagaDTO(
  String sagaId,
  String orderId,
  String status,
  String statusUrl
) {}
