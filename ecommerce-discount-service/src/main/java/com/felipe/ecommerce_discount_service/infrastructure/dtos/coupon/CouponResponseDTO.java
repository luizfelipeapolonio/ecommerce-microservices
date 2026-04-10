package com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.List;

public record CouponResponseDTO(
  String id,
  String name,
  String description,
  String code,
  String discountType,
  String discountValue,
  String minimumPrice,
  boolean isActive,
  int usageLimit,
  int usageCount,
  String endDate,
  String createdAt,
  String updatedAt,
  List<CouponAppliedByDTO> appliedBy
) {
  public CouponResponseDTO(Coupon coupon) {
    this(
      coupon.getId().toString(),
      coupon.getName(),
      coupon.getDescription(),
      coupon.getCouponCode(),
      coupon.getDiscountType(),
      coupon.getDiscountValue(),
      coupon.getMinimumPrice().toString(),
      coupon.isActive(),
      coupon.getUsageLimit(),
      coupon.getUsageCount(),
      coupon.getEndDate().toString(),
      coupon.getCreatedAt().toString(),
      coupon.getUpdatedAt().toString(),
      coupon.getAppliedBy().stream().map(CouponAppliedByDTO::new).toList()
    );
  }
}
