package com.felipe.ecommerce_discount_service.core.application.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.Optional;

public interface PromotionGateway {
  Optional<Promotion> createPromotion(Promotion promotion);
}
