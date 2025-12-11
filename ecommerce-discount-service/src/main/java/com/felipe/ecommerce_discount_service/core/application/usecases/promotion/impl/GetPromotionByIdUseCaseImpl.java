package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetPromotionByIdUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.UUID;

public class GetPromotionByIdUseCaseImpl implements GetPromotionByIdUseCase {
  private final PromotionGateway promotionGateway;

  public GetPromotionByIdUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public Promotion execute(UUID promotionId) {
    return this.promotionGateway.findPromotionById(promotionId)
      .orElseThrow(() -> new DataNotFoundException("Promoção de id: '" + promotionId + "' não encontrada"));
  }
}
