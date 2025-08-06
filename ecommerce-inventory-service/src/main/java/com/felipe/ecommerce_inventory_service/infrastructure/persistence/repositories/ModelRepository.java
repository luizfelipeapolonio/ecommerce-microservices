package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModelRepository extends JpaRepository<ModelEntity, Long> {
  Optional<ModelEntity> findByName(String name);

  @Query("SELECT m FROM ModelEntity m JOIN m.brand b WHERE b.id = :brandId")
  List<ModelEntity> findAllByBrandId(@Param("brandId") Long brandId);
}
