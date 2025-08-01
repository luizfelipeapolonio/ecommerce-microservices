package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BrandRepository extends JpaRepository<BrandEntity, Long> {
  Optional<BrandEntity> findByName(String name);
}
