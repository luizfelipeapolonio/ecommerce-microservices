package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateCartItemDTO(
  @NotNull(message = "A quantidade do produto é obrigatória")
  @Positive(message = "A quantidade deve ser um número positivo maior do que zero")
  Integer quantity
) {}
