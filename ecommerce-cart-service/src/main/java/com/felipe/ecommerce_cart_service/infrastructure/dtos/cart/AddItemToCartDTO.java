package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record AddItemToCartDTO(
  @NotNull(message = "O id produto é obrigatório")
  UUID productId,

  @NotNull(message = "A quantidade do produto é obrigatória")
  @Positive(message = "A quantidade deve ser um número positivo maior do que zero")
  Integer quantity
) {}
