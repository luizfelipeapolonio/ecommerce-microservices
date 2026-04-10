package com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {
  Optional<CouponEntity> findByCouponCodeAndIsActiveTrue(String couponCode);
}
