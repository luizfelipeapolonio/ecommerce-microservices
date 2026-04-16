package com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetCouponByIdUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.UUID;

public class GetCouponByIdUseCaseImpl implements GetCouponByIdUseCase {
  private final CouponGateway couponGateway;

  public GetCouponByIdUseCaseImpl(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Override
  public Coupon execute(UUID couponId) {
    return this.couponGateway.findCouponById(couponId)
      .orElseThrow(() -> new DataNotFoundException("Cupom de id '" + couponId + "' não encontrado"));
  }
}
