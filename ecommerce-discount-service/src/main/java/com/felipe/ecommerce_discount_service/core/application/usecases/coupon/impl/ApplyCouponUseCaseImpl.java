package com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl;

import com.felipe.ecommerce_discount_service.core.application.exceptions.CouponAlreadyAppliedException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidCouponException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.MinimumPriceException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.ApplyCouponUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;

import java.math.BigDecimal;
import java.util.UUID;

public class ApplyCouponUseCaseImpl implements ApplyCouponUseCase {
  private final CouponGateway couponGateway;

  public ApplyCouponUseCaseImpl(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Override
  public Coupon execute(String couponCode, String orderAmount, UUID orderId, UUID customerId) {
    Coupon coupon = this.couponGateway.findCouponByCode(couponCode)
      .orElseThrow(() -> new DataNotFoundException("Cupom '" + couponCode + "' não encontrado"));

    if (coupon.getUsageCount() >= coupon.getUsageLimit()) {
      throw new InvalidCouponException(couponCode);
    }

    BigDecimal orderPrice = new BigDecimal(orderAmount);
    if (orderPrice.compareTo(coupon.getMinimumPrice()) < 0) {
      throw new MinimumPriceException(
        "Cupom não aplicado! O valor mínimo da compra para aplicar o cupom é R$ " + coupon.getMinimumPrice().toPlainString()
      );
    }

    boolean isCouponAlreadyApplied = coupon.getAppliedBy()
      .stream()
      .anyMatch(appliedBy -> appliedBy.getOrderId().equals(orderId) || appliedBy.getCustomerId().equals(customerId));

    if (isCouponAlreadyApplied) {
      throw new CouponAlreadyAppliedException(couponCode);
    }

    CouponAppliedBy appliedBy = new CouponAppliedBy().orderId(orderId).customerId(customerId);
    coupon.addAppliedBy(appliedBy);
    coupon.increaseUsageCount();
    return this.couponGateway.saveCoupon(coupon);
  }
}
