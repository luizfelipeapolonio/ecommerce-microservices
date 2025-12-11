package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.List;

public class GetAllPromotionsUseCaseImpl implements GetAllPromotionsUseCase {
  private final PromotionGateway promotionGateway;

  public GetAllPromotionsUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public List<Promotion> execute() {
    return this.promotionGateway.findAllPromotions();
  }
}
