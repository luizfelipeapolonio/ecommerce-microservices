package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.CreatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.Optional;

public interface CreatePromotionUseCase {
  Optional<Promotion> execute(CreatePromotionDTO promotionDTO);
}
