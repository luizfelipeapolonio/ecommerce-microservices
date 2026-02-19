package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationGateway {
  Reservation reserveProduct(UUID productId, UUID orderId, int quantity);
  List<Reservation> findReservationsByProductId(UUID productId);
  Optional<Reservation> findReservationByProductIdAndOrderId(UUID productId, UUID orderId);
  void deleteReservation(Long reservationId);
}
