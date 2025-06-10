package com.felipe.ecommerce_customer_service.infrastructure.dtos;

public record AddressDTO(
  String street,
  String number,
  String complement,
  String district,
  String zipcode,
  String city,
  String state,
  String country
) {}
