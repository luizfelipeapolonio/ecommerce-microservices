package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
  List<ReservationEntity> findAllByOrderIdAndStatus(UUID orderId, String status);
  List<ReservationEntity> findByProductIdInAndStatus(List<UUID> productIds, String status);
  Optional<ReservationEntity> findByOrderId(UUID orderId);
}
