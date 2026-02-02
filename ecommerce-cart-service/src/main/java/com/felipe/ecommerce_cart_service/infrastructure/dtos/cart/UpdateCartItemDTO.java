package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemDTO(
  @NotNull(message = "A quantidade do produto é obrigatória")
  @Positive(message = "A quantidade deve ser um número positivo maior do que zero")
  @Schema(name = "quantity", type = "integer", example = "2")
  Integer quantity
) {}
