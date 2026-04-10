package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.CouponEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.CouponRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CouponGatewayImpl implements CouponGateway {
  private final CouponRepository couponRepository;
  private final CouponEntityMapper couponEntityMapper;

  public CouponGatewayImpl(CouponRepository couponRepository, CouponEntityMapper couponEntityMapper) {
    this.couponRepository = couponRepository;
    this.couponEntityMapper = couponEntityMapper;
  }

  @Override
  public Optional<Coupon> findCouponByCode(String couponCode) {
    return this.couponRepository.findByCouponCodeAndIsActiveTrue(couponCode).map(this.couponEntityMapper::toDomain);
  }

  @Override
  public Coupon saveCoupon(Coupon coupon) {
    CouponEntity entity = this.couponEntityMapper.toEntity(coupon);
    return this.couponEntityMapper.toDomain(this.couponRepository.save(entity));
  }
}
