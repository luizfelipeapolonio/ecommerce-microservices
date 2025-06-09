package com.felipe.ecommerce_customer_service.infrastructure.dtos;

public record CustomerResponseDTO(
  String id,
  String email,
  String username,
  String firstName,
  String lastName,
  String createdAt,
  String updatedAt
) {}
