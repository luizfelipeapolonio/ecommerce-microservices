package com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.RemoveCouponApplicationUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;

import java.util.Optional;
import java.util.UUID;

public class RemoveCouponApplicationUseCaseImpl implements RemoveCouponApplicationUseCase {
  private final CouponGateway couponGateway;

  public RemoveCouponApplicationUseCaseImpl(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Override
  public void execute(String couponCode, UUID customerId, UUID orderId) {
    Coupon coupon = this.couponGateway.findCouponByCode(couponCode)
      .orElseThrow(() -> new DataNotFoundException("Cupom '" + couponCode + "' não encontrado"));

    Optional<CouponAppliedBy> couponAppliedBy = coupon.getAppliedBy()
      .stream()
      .filter(appliedBy -> appliedBy.getCustomerId().equals(customerId) || appliedBy.getOrderId().equals(orderId))
      .findFirst();
    if (couponAppliedBy.isEmpty()) return;

    coupon.removeAppliedBy(couponAppliedBy.get());
    coupon.decreaseUsageCount();
    this.couponGateway.saveCoupon(coupon);
  }
}
