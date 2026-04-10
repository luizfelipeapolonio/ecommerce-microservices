package com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl;

import com.felipe.ecommerce_discount_service.core.application.dtos.EndDateDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.coupon.CreateCouponDTO;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CreateCouponUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class CreateCouponUseCaseImpl implements CreateCouponUseCase {
  private final CouponGateway couponGateway;

  public CreateCouponUseCaseImpl(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Override
  public Coupon execute(CreateCouponDTO couponDTO) {
    LocalDateTime endDate = convertEndDate(couponDTO.endDate());
    if (endDate.isBefore(LocalDateTime.now())) {
      throw new InvalidEndDateException(endDate.toString());
    }

    Coupon coupon = new Coupon()
      .name(couponDTO.name())
      .description(couponDTO.description())
      .couponCode(couponDTO.couponCode())
      .endDate(endDate)
      .discountType(DiscountType.of(couponDTO.discountType()))
      .discountValue(couponDTO.discountValue())
      .minimumPrice(new BigDecimal(couponDTO.minimumPrice()))
      .usageLimit(couponDTO.usageLimit());
    return this.couponGateway.saveCoupon(coupon);
  }

  private LocalDateTime convertEndDate(EndDateDTO endDate) {
    LocalDate date = LocalDate.of(endDate.year(), endDate.month(), endDate.dayOfMonth());
    LocalTime time = LocalTime.of(endDate.hour(), endDate.minute(), endDate.second());
    return LocalDateTime.of(date, time);
  }
}
