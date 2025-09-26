package com.felipe.ecommerce_discount_service.core.application.dtos.promotion;

import java.util.List;

public interface CreatePromotionDTO {
  String name();
  String description();
  String scope();
  String discountType();
  String discountValue();
  String minimumPrice();
  EndDateDTO endDate();
  List<? extends PromotionAppliesToDTO> promotionApplies();
}
