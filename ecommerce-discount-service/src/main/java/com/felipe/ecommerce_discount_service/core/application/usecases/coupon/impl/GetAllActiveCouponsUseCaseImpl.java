package com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl;

import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetAllActiveCouponsUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.List;

public class GetAllActiveCouponsUseCaseImpl implements GetAllActiveCouponsUseCase {
  private final CouponGateway couponGateway;

  public GetAllActiveCouponsUseCaseImpl(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Override
  public List<Coupon> execute() {
    return this.couponGateway.findAllActiveCoupons();
  }
}
