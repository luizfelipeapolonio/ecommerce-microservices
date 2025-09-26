package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.PromotionAppliesToDTO;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionAppliesToEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PromotionAppliesToDTOImpl(
  @NotBlank(message = "O alvo da promoção é obrigatório")
  @Pattern(regexp = "^(category|brand|model|product)$", message = "Alvo inválido! Os alvos válidos são 'category', 'brand', 'model' e 'product'")
  String target,

  @NotBlank(message = "O id do alvo da promoção é obrigatório")
  String targetId
) implements PromotionAppliesToDTO {
  public PromotionAppliesToDTOImpl(PromotionAppliesToEntity promotionAppliesTo) {
    this(promotionAppliesTo.getTarget(), promotionAppliesTo.getTargetId());
  }
}
