package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.UpdatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.UUID;

public interface UpdatePromotionUseCase {
  Promotion execute(UUID promotionId, UpdatePromotionDTO promotionDTO);
}
