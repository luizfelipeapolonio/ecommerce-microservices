package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationGateway {
  List<Reservation> reserveProduct(UUID orderId, List<ProductReservationDTO> reservationDTOs);
  Optional<Reservation> findReservationByOrderId(UUID orderId);
  List<Reservation> findAllReservationsByOrderIdAndStatus(UUID orderId, ReservationStatus status);
  List<Reservation> findReservationsByProductIds(List<UUID> productIds);
  void updateReservations(List<Reservation> reservations);
  void deleteReservations(List<Reservation> reservations);
}
