package com.felipe.ecommerce_discount_service.core.application.dtos.coupon;

import com.felipe.ecommerce_discount_service.core.application.dtos.EndDateDTO;

public interface CreateCouponDTO {
  String name();
  String description();
  String couponCode();
  String discountType();
  String discountValue();
  String minimumPrice();
  EndDateDTO endDate();
  Integer usageLimit();
}
