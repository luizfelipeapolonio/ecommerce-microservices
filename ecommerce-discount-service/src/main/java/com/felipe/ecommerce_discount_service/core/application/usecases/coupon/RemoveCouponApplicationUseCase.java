package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import java.util.UUID;

public interface RemoveCouponApplicationUseCase {
  void execute(String couponCode, UUID customerId, UUID orderId);
}
