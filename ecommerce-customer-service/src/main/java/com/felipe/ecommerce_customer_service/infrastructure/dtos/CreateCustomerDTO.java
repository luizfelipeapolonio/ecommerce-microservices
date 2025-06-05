package com.felipe.ecommerce_customer_service.infrastructure.dtos;

public record CreateCustomerDTO(
  String email,
  String firstName,
  String lastName,
  String username,
  String password
) {}
