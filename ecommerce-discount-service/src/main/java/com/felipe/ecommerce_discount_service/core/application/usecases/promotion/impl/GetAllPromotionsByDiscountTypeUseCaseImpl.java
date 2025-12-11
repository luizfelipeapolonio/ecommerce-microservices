package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsByDiscountTypeUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;

import java.util.List;

public class GetAllPromotionsByDiscountTypeUseCaseImpl implements GetAllPromotionsByDiscountTypeUseCase {
  private final PromotionGateway promotionGateway;

  public GetAllPromotionsByDiscountTypeUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public List<Promotion> execute(String discountType) {
    final DiscountType discount = DiscountType.of(discountType);
    return this.promotionGateway.findAllPromotionsByDiscountType(discount);
  }
}
