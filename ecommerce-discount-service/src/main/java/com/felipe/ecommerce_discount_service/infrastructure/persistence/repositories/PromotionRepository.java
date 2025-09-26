package com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PromotionRepository extends JpaRepository<PromotionEntity, UUID> {
}
