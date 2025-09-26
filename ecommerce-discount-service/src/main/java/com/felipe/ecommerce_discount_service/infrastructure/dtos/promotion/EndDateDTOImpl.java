package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.EndDateDTO;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record EndDateDTOImpl(
  @NotNull(message = "O dia do mês é obrigatório")
  @Range(min = 1, max = 31, message = "O dia do mês deve ser um número entre 1 e 31")
  int dayOfMonth,

  @NotNull(message = "O mês é obrigatório")
  @Range(min = 1, max = 12, message = "O mês deve ser um número entre 1 e 12")
  int month,

  @NotNull(message = "O ano é obrigatório")
  int year,

  @NotNull(message = "A hora é obrigatória")
  @Range(min = 0, max = 23, message = "A hora deve ser um número entre 0 e 23")
  int hour,

  @NotNull(message = "O minuto é obrigatório")
  @Range(min = 0, max = 59, message = "O minuto deve ser um número entre 0 e 59")
  int minute,

  @NotNull(message = "O segundo é obrigatório")
  @Range(min = 0, max = 59, message = "O segundo deve ser um número entre 0 e 59")
  int second
) implements EndDateDTO {}
