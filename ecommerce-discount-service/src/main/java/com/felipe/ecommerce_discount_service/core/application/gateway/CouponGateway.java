package com.felipe.ecommerce_discount_service.core.application.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponGateway {
  Coupon createCoupon(Coupon coupon);
  Optional<Coupon> findCouponByCode(String couponCode);
  Optional<Coupon> findCouponById(UUID couponId);
  List<Coupon> findAllActiveCoupons();
  List<Coupon> findAllCoupons();
  Coupon saveCoupon(Coupon coupon);
  Coupon deleteCoupon(Coupon coupon);
}
