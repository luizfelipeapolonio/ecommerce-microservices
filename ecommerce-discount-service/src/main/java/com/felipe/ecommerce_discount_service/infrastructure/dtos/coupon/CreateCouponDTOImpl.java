package com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon;

import com.felipe.ecommerce_discount_service.core.application.dtos.coupon.CreateCouponDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.EndDateDTOImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record CreateCouponDTOImpl(
  @NotBlank(message = "O nome do cupom é obrigatório e não deve estar em branco")
  @Length(max = 150, message = "O nome do cupom deve ter no máximo 150 caracteres")
  @Schema(name = "name", type = "string", example = "Coupon 20% OFF", maxLength = 150)
  String name,

  @Nullable
  @Schema(name = "description", type = "string", example = "Description of coupon", nullable = true)
  String description,

  @NotBlank(message = "O código do cupom é obrigatório e não deve estar em branco")
  @Length(max = 30, message = "O código do cupom deve ter no máximo 30 caracteres")
  @Schema(name = "couponCode", type = "string", example = "COUPON20OFF", maxLength = 30)
  String couponCode,

  @NotBlank(message = "O tipo do desconto é obrigatório")
  @Pattern(regexp = "^(fixed_amount|percentage)$", message = "Tipo de desconto inválido! Os valores aceitos são 'fixed_amount' e 'percentage'")
  @Schema(name = "discountType", type = "string", pattern = "^(fixed_amount|percentage)$", example = "percentage")
  String discountType,

  @NotBlank(message = "O valor do desconto é obrigatório")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 80.00")
  @Schema(name = "discountValue", type = "string", pattern = "^\\d+\\.\\d{2}$", example = "20.00")
  String discountValue,

  @NotBlank(message = "O preço mínimo do cupom é obrigatório")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 1200.00")
  @Schema(name = "minimumPrice", type = "string", pattern = "^\\d+\\.\\d{2}$", example = "50.00")
  String minimumPrice,

  @Valid
  @NotNull(message = "A data de término do cupom é obrigatória")
  @Schema(name = "endDate", type = "object")
  EndDateDTOImpl endDate,

  @NotNull(message = "O limite de uso do cupom é obrigatório")
  @Positive(message = "O limite de uso deve ser um número positivo maior do que zero")
  @Schema(name = "usageLimit", type = "integer", format = "int32", example = "30")
  Integer usageLimit
) implements CreateCouponDTO {}
