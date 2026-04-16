package com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponRepository extends JpaRepository<CouponEntity, UUID> {
  Optional<CouponEntity> findByCouponCodeAndIsActiveTrue(String couponCode);
  List<CouponEntity> findAllByIsActiveTrue();

  @Query("SELECT c FROM CouponEntity c WHERE c.endDate > CURRENT_TIMESTAMP AND c.isActive = true")
  List<CouponEntity> findAllByEndDateAfterNowAndIsActiveTrue();
}
