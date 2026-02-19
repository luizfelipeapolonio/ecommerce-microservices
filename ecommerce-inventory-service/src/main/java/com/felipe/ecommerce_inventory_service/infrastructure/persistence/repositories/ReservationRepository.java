package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
  List<ReservationEntity> findAllByProductId(UUID productId);
  Optional<ReservationEntity> findByProductIdAndOrderId(UUID productId, UUID orderId);
}
