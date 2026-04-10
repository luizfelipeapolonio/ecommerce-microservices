package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;

public interface CheckIfCouponIsValidUseCase {
  Coupon execute(String couponCode);
}
