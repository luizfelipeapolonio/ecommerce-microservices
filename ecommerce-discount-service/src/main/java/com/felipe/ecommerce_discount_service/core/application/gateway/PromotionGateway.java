package com.felipe.ecommerce_discount_service.core.application.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.Optional;
import java.util.UUID;

public interface PromotionGateway {
  Optional<Promotion> createPromotion(Promotion promotion);
  Optional<Promotion> findPromotionById(UUID promotionId);
  Promotion deletePromotion(Promotion promotion);
}
