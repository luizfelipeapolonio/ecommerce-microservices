package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.UpdatePromotionDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.Length;

public record UpdatePromotionDTOImpl(
  @Nullable
  @Length(max = 150, message = "O nome da promoção deve ter no máximo 150 caracteres")
  @Schema(name = "name", type = "string", example = "50% OFF")
  String name,

  @Nullable
  @Schema(name = "description", type = "string", nullable = true, example = "50% off discount")
  String description,

  @Nullable
  @Valid
  @Schema(name = "endDate", type = "object")
  EndDateDTOImpl endDate
) implements UpdatePromotionDTO {}
