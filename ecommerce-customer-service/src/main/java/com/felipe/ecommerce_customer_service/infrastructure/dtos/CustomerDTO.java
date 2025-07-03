package com.felipe.ecommerce_customer_service.infrastructure.dtos;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerDTO(
  @Schema(name = "id", type = "string", example = "f6043015-5e69-435f-98eb-94ad328d3a4a")
  String id,

  @Schema(name = "email", type = "string", example = "john@email.com")
  String email,

  @Schema(name = "username", type = "string", example = "johndoe")
  String username,

  @Schema(name = "firstName", type = "string", example = "John")
  String firstName,

  @Schema(name = "lastName", type = "string", example = "Doe")
  String lastName,

  @Schema(name = "createdAt", type = "string", example = "2025-06-09 18:32:26.711469")
  String createdAt,

  @Schema(name = "updatedAt", type = "string", example = "2025-06-09 18:32:26.711469")
  String updatedAt
) {}
