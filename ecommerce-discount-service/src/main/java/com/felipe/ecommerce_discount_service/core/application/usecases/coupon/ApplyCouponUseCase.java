package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.UUID;

public interface ApplyCouponUseCase {
  Coupon execute(String couponCode, String orderAmount, UUID orderId, UUID customerId);
}
