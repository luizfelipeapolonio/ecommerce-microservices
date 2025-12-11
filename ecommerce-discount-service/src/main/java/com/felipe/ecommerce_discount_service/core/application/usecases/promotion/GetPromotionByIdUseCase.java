package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.UUID;

public interface GetPromotionByIdUseCase {
  Promotion execute(UUID promotionId);
}
