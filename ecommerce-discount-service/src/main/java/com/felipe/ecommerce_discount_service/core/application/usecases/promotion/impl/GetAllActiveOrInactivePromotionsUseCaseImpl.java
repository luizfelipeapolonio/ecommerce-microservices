package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllActiveOrInactivePromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.List;

public class GetAllActiveOrInactivePromotionsUseCaseImpl implements GetAllActiveOrInactivePromotionsUseCase {
  private final PromotionGateway promotionGateway;

  public GetAllActiveOrInactivePromotionsUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public List<Promotion> execute(boolean isActive) {
    return this.promotionGateway.findAllActiveOrInactivePromotions(isActive);
  }
}
