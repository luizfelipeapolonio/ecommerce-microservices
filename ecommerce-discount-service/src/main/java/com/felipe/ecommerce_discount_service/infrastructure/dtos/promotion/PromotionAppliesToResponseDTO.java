package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;

public record PromotionAppliesToResponseDTO(long id, String target, String targetId, String appliedAt) {
  public PromotionAppliesToResponseDTO(PromotionAppliesTo promotionAppliesTo) {
    this(
      promotionAppliesTo.getId(),
      promotionAppliesTo.getTarget(),
      promotionAppliesTo.getTargetId(),
      promotionAppliesTo.getAppliedAt().toString()
    );
  }
}
