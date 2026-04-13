package com.felipe.ecommerce_discount_service.infrastructure.config.beans;

import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.ApplyCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CreateCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CheckIfCouponIsValidUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.RemoveCouponApplicationUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.ApplyCouponUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.CreateCouponUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.CheckIfCouponIsValidUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.RemoveCouponApplicationUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CouponBeans {
  private final CouponGateway couponGateway;

  public CouponBeans(CouponGateway couponGateway) {
    this.couponGateway = couponGateway;
  }

  @Bean
  public CreateCouponUseCase createCouponUseCase() {
    return new CreateCouponUseCaseImpl(this.couponGateway);
  }

  @Bean
  public CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase() {
    return new CheckIfCouponIsValidUseCaseImpl(this.couponGateway);
  }

  @Bean
  public ApplyCouponUseCase applyCouponUseCase() {
    return new ApplyCouponUseCaseImpl(this.couponGateway);
  }

  @Bean
  public RemoveCouponApplicationUseCase removeCouponApplicationUseCase() {
    return new RemoveCouponApplicationUseCaseImpl(this.couponGateway);
  }
}
