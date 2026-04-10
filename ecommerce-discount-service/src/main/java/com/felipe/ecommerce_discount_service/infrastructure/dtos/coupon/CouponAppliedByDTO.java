package com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon;

import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;

public record CouponAppliedByDTO(String orderId, String customerId) {
  public CouponAppliedByDTO(CouponAppliedBy appliedBy) {
    this(appliedBy.getOrderId().toString(), appliedBy.getCustomerId().toString());
  }
}
