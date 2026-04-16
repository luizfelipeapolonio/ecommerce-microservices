package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;

import java.util.List;

public interface GetAllCouponsUseCase {
  List<Coupon> execute();
}
