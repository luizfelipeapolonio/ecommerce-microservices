package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationEntityMapper {
  public Reservation toDomain(ReservationEntity reservationEntity) {
    return new Reservation()
      .id(reservationEntity.getId())
      .productId(reservationEntity.getProductId())
      .orderId(reservationEntity.getOrderId())
      .quantity(reservationEntity.getQuantity())
      .status(ReservationStatus.of(reservationEntity.getStatus()))
      .createdAt(reservationEntity.getCreatedAt())
      .updatedAt(reservationEntity.getUpdatedAt());
  }

  public ReservationEntity toEntity(Reservation reservation) {
    return new ReservationEntity()
      .id(reservation.getId())
      .productId(reservation.getProductId())
      .orderId(reservation.getOrderId())
      .quantity(reservation.getQuantity())
      .status(ReservationStatus.of(reservation.getStatus()))
      .createdAt(reservation.getCreatedAt())
      .updatedAt(reservation.getUpdatedAt());
  }
}
