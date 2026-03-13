package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderProductDTO;

import java.util.UUID;

public record CreateOrderProductDTOImpl(UUID id, long quantity) implements CreateOrderProductDTO {
}
