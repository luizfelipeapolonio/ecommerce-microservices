package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.DeletePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.UUID;

public class DeletePromotionUseCaseImpl implements DeletePromotionUseCase {
  private final PromotionGateway promotionGateway;

  public DeletePromotionUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public Promotion execute(UUID promotionId) {
    final Promotion promotion = this.promotionGateway.findPromotionById(promotionId)
      .orElseThrow(() -> new DataNotFoundException("Promoção de id: '" + promotionId + "' não encontrada"));
    return this.promotionGateway.deletePromotion(promotion);
  }
}
