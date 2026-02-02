package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCartDTO(
  @NotNull(message = "O id do usuário é obrigatório")
  @Schema(name = "customerId", type = "string", example = "da4dd8a3-a821-4350-9af2-c5b8f3801330")
  UUID customerId
) {}
