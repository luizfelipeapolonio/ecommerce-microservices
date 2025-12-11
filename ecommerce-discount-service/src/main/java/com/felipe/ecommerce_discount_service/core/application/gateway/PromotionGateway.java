package com.felipe.ecommerce_discount_service.core.application.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionGateway {
  Optional<Promotion> createPromotion(Promotion promotion);
  Optional<Promotion> findPromotionById(UUID promotionId);
  Promotion deletePromotion(Promotion promotion);
  Optional<Promotion> findActivePromotionById(UUID promotionId);
  Promotion updatePromotion(Promotion promotion);
  List<Promotion> findAllPromotions();
  List<Promotion> findAllActiveOrInactivePromotions(boolean isActive);
  List<Promotion> findAllPromotionsByDiscountType(DiscountType discountType);
}
