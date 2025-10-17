package com.felipe.ecommerce_discount_service.infrastructure.mappers;

import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionAppliesToEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PromotionEntityMapper {
  public PromotionEntity toEntity(Promotion promotion) {
    return PromotionEntity.builder()
      .id(promotion.getId())
      .name(promotion.getName())
      .description(promotion.getDescription())
      .scope(PromotionScope.of(promotion.getScope()))
      .discountType(DiscountType.of(promotion.getDiscountType()))
      .discountValue(promotion.getDiscountValue())
      .minimumPrice(promotion.getMinimumPrice())
      .endDate(promotion.getEndDate())
      .isActive(promotion.isActive())
      .createdAt(promotion.getCreatedAt())
      .updatedAt(promotion.getUpdatedAt())
      .promotionApplies(promotionAppliesToEntityList(promotion.getPromotionApplies()))
      .build();
  }

  public Promotion toDomain(PromotionEntity promotion) {
    return Promotion.builder()
      .id(promotion.getId())
      .name(promotion.getName())
      .description(promotion.getDescription())
      .scope(PromotionScope.of(promotion.getScope()))
      .discountType(DiscountType.of(promotion.getDiscountType()))
      .discountValue(promotion.getDiscountValue())
      .minimumPrice(promotion.getMinimumPrice())
      .endDate(promotion.getEndDate())
      .isActive(promotion.isActive())
      .createdAt(promotion.getCreatedAt())
      .updatedAt(promotion.getUpdatedAt())
      .promotionApplies(promotionAppliesEntityToDomainList(promotion.getPromotionApplies()))
      .build();
  }

  private PromotionAppliesToEntity promotionAppliesToEntity(PromotionAppliesTo promotionAppliesTo) {
    final PromotionAppliesToEntity promotionApplies = new PromotionAppliesToEntity();
    promotionApplies.setId(promotionAppliesTo.getId());
    promotionApplies.setTarget(PromotionAppliesTarget.of(promotionAppliesTo.getTarget()));
    promotionApplies.setTargetId(promotionAppliesTo.getTargetId());
    promotionApplies.setAppliedAt(promotionAppliesTo.getAppliedAt());
    return promotionApplies;
  }

  private PromotionAppliesTo promotionAppliesEntityToDomain(PromotionAppliesToEntity promotionAppliesTo) {
    final PromotionAppliesTo promotionApplies = new PromotionAppliesTo();
    promotionApplies.setId(promotionAppliesTo.getId());
    promotionApplies.setTarget(PromotionAppliesTarget.of(promotionAppliesTo.getTarget()));
    promotionApplies.setTargetId(promotionAppliesTo.getTargetId());
    promotionApplies.setAppliedAt(promotionAppliesTo.getAppliedAt());
    return promotionApplies;
  }

  private List<PromotionAppliesToEntity> promotionAppliesToEntityList(List<PromotionAppliesTo> promotionApplies) {
    return promotionApplies.stream().map(this::promotionAppliesToEntity).toList();
  }

  private List<PromotionAppliesTo> promotionAppliesEntityToDomainList(List<PromotionAppliesToEntity> promotionApplies) {
    return promotionApplies.stream().map(this::promotionAppliesEntityToDomain).toList();
  }
}
