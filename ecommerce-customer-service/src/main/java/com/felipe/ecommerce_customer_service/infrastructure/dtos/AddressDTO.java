package com.felipe.ecommerce_customer_service.infrastructure.dtos;

import jakarta.validation.constraints.NotBlank;

public record AddressDTO(
  @NotBlank(message = "O nome da rua é obrigatório e não deve estar em branco")
  String street,

  @NotBlank(message = "O número é obrigatório e não deve estar em branco")
  String number,

  @NotBlank(message = "O complemento é obrigatório e não deve estar em branco")
  String complement,

  @NotBlank(message = "O bairro é obrigatório e não deve estar em branco")
  String district,

  @NotBlank(message = "O CEP é obrigatório e não deve estar em branco")
  String zipcode,

  @NotBlank(message = "A cidade é obrigatória e não deve estar em branco")
  String city,

  @NotBlank(message = "O estado é obrigatório e não deve estar em branco")
  String state,

  @NotBlank(message = "O país é obrigatório e não deve estar em branco")
  String country
) {}
