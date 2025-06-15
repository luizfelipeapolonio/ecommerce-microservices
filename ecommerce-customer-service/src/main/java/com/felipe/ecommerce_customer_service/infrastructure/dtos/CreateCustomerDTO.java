package com.felipe.ecommerce_customer_service.infrastructure.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateCustomerDTO(
  @NotBlank(message = "O e-mail é obrigatório e não deve estar em branco")
  @Email(message = "Formato de e-mail inválido")
  String email,

  @NotBlank(message = "O primeiro nome é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O primeiro nome deve ter até 100 caracteres")
  String firstName,

  @NotBlank(message = "O último nome é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O último nome deve ter até 100 caracteres")
  String lastName,

  @NotBlank(message = "O username é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O username deve ter até 100 caracteres")
  String username,

  @NotBlank(message = "A senha é obrigatória e não deve estar em branco")
  @Length(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
  String password
) {}
