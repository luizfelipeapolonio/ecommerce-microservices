package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockProductQuantityDTO(
  @NotNull(message = "A quantidade de produtos é obrigatória")
  @Positive(message = "A quantidade de produtos deve ser um número positivo maior do que zero")
  Long quantity
) {}
