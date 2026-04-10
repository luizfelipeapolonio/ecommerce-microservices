package com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon;

import com.felipe.ecommerce_discount_service.core.application.dtos.coupon.CreateCouponDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.EndDateDTOImpl;
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
  String name,

  @Nullable
  String description,

  @NotBlank(message = "O código do cupom é obrigatório e não deve estar em branco")
  @Length(max = 30, message = "O código do cupom deve ter no máximo 30 caracteres")
  String couponCode,

  @NotBlank(message = "O tipo do desconto é obrigatório")
  @Pattern(regexp = "^(fixed_amount|percentage)$", message = "Tipo de desconto inválido! Os valores aceitos são 'fixed_amount' e 'percentage'")
  String discountType,

  @NotBlank(message = "O valor do desconto é obrigatório")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 80.00")
  String discountValue,

  @NotBlank(message = "O preço mínimo do cupom é obrigatório")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 1200.00")
  String minimumPrice,

  @Valid
  @NotNull(message = "A data de término do cupom é obrigatória")
  EndDateDTOImpl endDate,

  @NotNull(message = "O limite de uso do cupom é obrigatório")
  @Positive(message = "O limite de uso deve ser um número positivo maior do que zero")
  Integer usageLimit
) implements CreateCouponDTO {}
