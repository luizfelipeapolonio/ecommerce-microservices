package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationGateway {
  List<Reservation> reserveProduct(UUID orderId, List<ProductReservationDTO> reservationDTOs);
  Optional<Reservation> findReservationByOrderId(UUID orderId);
  List<Reservation> findAllReservationsByOrderId(UUID orderId);
  List<Reservation> findReservationsByProductIds(List<UUID> productIds);
  void deleteReservations(List<Reservation> reservations);
}
