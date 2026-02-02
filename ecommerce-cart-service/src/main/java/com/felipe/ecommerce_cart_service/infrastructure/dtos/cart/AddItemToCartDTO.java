package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AddItemToCartDTO(
  @NotNull(message = "O id produto é obrigatório")
  @Schema(name = "productId", type = "string", example = "ee2ec142-f3b5-494a-9857-b67abf606f9c")
  UUID productId,

  @NotNull(message = "A quantidade do produto é obrigatória")
  @Positive(message = "A quantidade deve ser um número positivo maior do que zero")
  @Schema(name = "quantity", type = "integer", example = "1")
  Integer quantity
) {}
