package com.felipe.ecommerce_discount_service.infrastructure.mappers;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponAppliedByEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import org.springframework.stereotype.Component;

@Component
public class CouponEntityMapper {
  public Coupon toDomain(CouponEntity entity) {
    Coupon coupon = new Coupon()
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .couponCode(entity.getCouponCode())
      .discountType(DiscountType.of(entity.getDiscountType()))
      .discountValue(entity.getDiscountValue())
      .endDate(entity.getEndDate())
      .minimumPrice(entity.getMinimumPrice())
      .usageLimit(entity.getUsageLimit())
      .usageCount(entity.getUsageCount())
      .isActive(entity.isActive())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt());
    entity.getAppliedBy().forEach(appliedByEntity -> {
      CouponAppliedBy appliedByDomain = toCouponAppliedByDomain(appliedByEntity);
      coupon.addAppliedBy(appliedByDomain);
    });
    return coupon;
  }

  public CouponEntity toEntity(Coupon coupon) {
    CouponEntity entity = new CouponEntity()
      .id(coupon.getId())
      .name(coupon.getName())
      .description(coupon.getDescription())
      .couponCode(coupon.getCouponCode())
      .discountType(DiscountType.of(coupon.getDiscountType()))
      .discountValue(coupon.getDiscountValue())
      .endDate(coupon.getEndDate())
      .minimumPrice(coupon.getMinimumPrice())
      .usageLimit(coupon.getUsageLimit())
      .usageCount(coupon.getUsageCount())
      .isActive(coupon.isActive())
      .createdAt(coupon.getCreatedAt())
      .updatedAt(coupon.getUpdatedAt());
    coupon.getAppliedBy().forEach(appliedBy -> {
      CouponAppliedByEntity appliedByEntity = toCouponAppliedByEntity(appliedBy);
      entity.addAppliedBy(appliedByEntity);
    });
    return entity;
  }

  private CouponAppliedBy toCouponAppliedByDomain(CouponAppliedByEntity entity) {
    return new CouponAppliedBy()
      .id(entity.getId())
      .orderId(entity.getOrderId())
      .customerId(entity.getCustomerId())
      .appliedAt(entity.getAppliedAt());
  }

  private CouponAppliedByEntity toCouponAppliedByEntity(CouponAppliedBy appliedFor) {
    return new CouponAppliedByEntity()
      .id(appliedFor.getId())
      .orderId(appliedFor.getOrderId())
      .customerId(appliedFor.getCustomerId())
      .appliedAt(appliedFor.getAppliedAt());
  }
}
