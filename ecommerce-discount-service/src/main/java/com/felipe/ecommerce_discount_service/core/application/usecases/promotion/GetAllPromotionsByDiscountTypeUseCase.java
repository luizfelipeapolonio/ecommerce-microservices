package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.util.List;

public interface GetAllPromotionsByDiscountTypeUseCase {
  List<Promotion> execute(String discountType);
}
