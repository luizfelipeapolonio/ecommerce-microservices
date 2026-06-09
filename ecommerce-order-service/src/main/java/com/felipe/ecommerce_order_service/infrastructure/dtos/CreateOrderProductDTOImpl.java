package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderProductDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record CreateOrderProductDTOImpl(
  @Schema(type = "string", example = "c2d693f0-ab78-4a88-8dae-a43787e6ab2f")
  UUID id,
  @Schema(type = "integer", example = "1")
  long quantity
) implements CreateOrderProductDTO {}
