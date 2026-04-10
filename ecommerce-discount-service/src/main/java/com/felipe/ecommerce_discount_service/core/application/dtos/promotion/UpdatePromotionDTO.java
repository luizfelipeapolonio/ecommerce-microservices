package com.felipe.ecommerce_discount_service.core.application.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.EndDateDTO;

public interface UpdatePromotionDTO {
  String name();
  String description();
  EndDateDTO endDate();
}
