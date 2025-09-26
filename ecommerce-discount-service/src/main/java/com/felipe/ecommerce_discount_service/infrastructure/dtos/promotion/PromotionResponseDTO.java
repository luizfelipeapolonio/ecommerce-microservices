package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;

import java.util.List;

public record PromotionResponseDTO(
  String id,
  String name,
  String description,
  String scope,
  String discountType,
  String discountValue,
  String minimumPrice,
  String endDate,
  boolean isActive,
  String createdAt,
  String updatedAt,
  List<PromotionAppliesToResponseDTO> promotionAppliesTo
) {
  public PromotionResponseDTO(Promotion promotion) {
    this(
      promotion.getId().toString(),
      promotion.getName(),
      promotion.getDescription(),
      promotion.getScope(),
      promotion.getDiscountType(),
      promotion.getDiscountValue(),
      promotion.getMinimumPrice().toString(),
      promotion.getEndDate().toString(),
      promotion.isActive(),
      promotion.getCreatedAt().toString(),
      promotion.getUpdatedAt().toString(),
      convert(promotion.getPromotionApplies())
    );
  }

  private static List<PromotionAppliesToResponseDTO> convert(List<PromotionAppliesTo> appliesTo) {
    return appliesTo.stream().map(PromotionAppliesToResponseDTO::new).toList();
  }
}
