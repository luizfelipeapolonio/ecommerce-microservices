package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.EndDateDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record EndDateDTOImpl(
  @NotNull(message = "O dia do mês é obrigatório")
  @Range(min = 1, max = 31, message = "O dia do mês deve ser um número entre 1 e 31")
  @Schema(name = "dayOfMonth", type = "integer", format = "int32", example = "22")
  int dayOfMonth,

  @NotNull(message = "O mês é obrigatório")
  @Range(min = 1, max = 12, message = "O mês deve ser um número entre 1 e 12")
  @Schema(name = "month", type = "integer", format = "int32", example = "6")
  int month,

  @NotNull(message = "O ano é obrigatório")
  @Schema(name = "year", type = "integer", format = "int32", example = "2025")
  int year,

  @NotNull(message = "A hora é obrigatória")
  @Range(min = 0, max = 23, message = "A hora deve ser um número entre 0 e 23")
  @Schema(name = "hour", type = "integer", format = "int32", example = "14")
  int hour,

  @NotNull(message = "O minuto é obrigatório")
  @Range(min = 0, max = 59, message = "O minuto deve ser um número entre 0 e 59")
  @Schema(name = "minute", type = "integer", format = "int32", example = "0")
  int minute,

  @NotNull(message = "O segundo é obrigatório")
  @Range(min = 0, max = 59, message = "O segundo deve ser um número entre 0 e 59")
  @Schema(name = "second", type = "integer", format = "int32", example = "0")
  int second
) implements EndDateDTO {}
