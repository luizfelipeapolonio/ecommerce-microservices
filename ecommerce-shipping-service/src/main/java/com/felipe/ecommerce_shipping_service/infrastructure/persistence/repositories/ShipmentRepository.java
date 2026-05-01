package com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_shipping_service.core.domain.ShipmentStatus;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipment.ShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ShipmentRepository extends JpaRepository<ShipmentEntity, UUID> {

  @Query("SELECT s FROM ShipmentEntity s WHERE s.status <> :status")
  List<ShipmentEntity> findAllWithStatusNotEqualTo(@Param("status") ShipmentStatus status);
}
