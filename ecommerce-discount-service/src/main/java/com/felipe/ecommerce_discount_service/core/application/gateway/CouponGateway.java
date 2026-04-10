package com.felipe.ecommerce_discount_service.core.application.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.Optional;

public interface CouponGateway {
  Optional<Coupon> findCouponByCode(String couponCode);
  Coupon saveCoupon(Coupon coupon);
}
