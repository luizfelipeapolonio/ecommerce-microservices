package com.felipe.ecommerce_cart_service.infrastructure.dtos.cart;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCartDTO(
  @NotNull(message = "O id do usuário é obrigatório")
  UUID customerId
) {}
