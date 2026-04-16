package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.CouponEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.CouponRepository;
import com.felipe.ecommerce_discount_service.infrastructure.services.DiscountSchedulerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CouponGatewayImpl implements CouponGateway {
  private final CouponRepository couponRepository;
  private final CouponEntityMapper couponEntityMapper;
  private final DiscountSchedulerService discountSchedulerService;

  public CouponGatewayImpl(CouponRepository couponRepository, CouponEntityMapper couponEntityMapper,
                           DiscountSchedulerService discountSchedulerService) {
    this.couponRepository = couponRepository;
    this.couponEntityMapper = couponEntityMapper;
    this.discountSchedulerService = discountSchedulerService;
  }

  @Override
  public Coupon createCoupon(Coupon coupon) {
    CouponEntity couponEntity = this.couponEntityMapper.toEntity(coupon);
    CouponEntity savedCoupon = this.couponRepository.save(couponEntity);
    this.discountSchedulerService.scheduleCouponToExpire(savedCoupon);
    return this.couponEntityMapper.toDomain(savedCoupon);
  }

  @Override
  public Optional<Coupon> findCouponByCode(String couponCode) {
    return this.couponRepository.findByCouponCodeAndIsActiveTrue(couponCode).map(this.couponEntityMapper::toDomain);
  }

  @Override
  public Optional<Coupon> findCouponById(UUID couponId) {
    return this.couponRepository.findById(couponId).map(this.couponEntityMapper::toDomain);
  }

  @Override
  public List<Coupon> findAllActiveCoupons() {
    return this.couponRepository.findAllByIsActiveTrue()
      .stream()
      .map(this.couponEntityMapper::toDomain)
      .toList();
  }

  @Override
  public List<Coupon> findAllCoupons() {
    return this.couponRepository.findAll()
      .stream()
      .map(this.couponEntityMapper::toDomain)
      .toList();
  }

  @Override
  public Coupon saveCoupon(Coupon coupon) {
    CouponEntity entity = this.couponEntityMapper.toEntity(coupon);
    return this.couponEntityMapper.toDomain(this.couponRepository.save(entity));
  }

  @Override
  public Coupon deleteCoupon(Coupon coupon) {
    CouponEntity entity = this.couponEntityMapper.toEntity(coupon);
    this.couponRepository.delete(entity);
    return coupon;
  }
}
