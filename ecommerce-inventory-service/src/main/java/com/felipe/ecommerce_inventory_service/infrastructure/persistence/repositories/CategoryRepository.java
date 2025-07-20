package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
  Optional<CategoryEntity> findByName(String name);
}
