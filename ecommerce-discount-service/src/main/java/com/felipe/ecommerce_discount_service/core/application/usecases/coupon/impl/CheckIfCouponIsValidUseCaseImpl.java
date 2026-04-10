package com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidCouponException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CheckIfCouponIsValidUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;

public class CheckIfCouponIsValidUseCaseImpl implements CheckIfCouponIsValidUseCase {
  private final CouponGateway couponGateway;

  public CheckIfCouponIsValidUseCaseImpl(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Override
  public Coupon execute(String couponCode) {
    Coupon coupon = this.couponGateway.findCouponByCode(couponCode)
      .orElseThrow(() -> new DataNotFoundException("Cupom de código '" + couponCode + "' não encontrado"));

    if (coupon.getUsageCount() >= coupon.getUsageLimit()) {
      throw new InvalidCouponException(couponCode);
    }
    return coupon;
  }
}
