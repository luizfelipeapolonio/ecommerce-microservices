package com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PromotionRepository extends JpaRepository<PromotionEntity, UUID> {

  @Query("SELECT p FROM PromotionEntity p WHERE p.endDate > CURRENT_TIMESTAMP AND p.isActive = true")
  List<PromotionEntity> findAllByEndDateAfterNowAndIsActiveTrue();

  Optional<PromotionEntity> findByIdAndIsActiveTrue(UUID id);
  List<PromotionEntity> findAllByIsActive(boolean isActive);
  List<PromotionEntity> findAllByDiscountType(String discountType);
}
