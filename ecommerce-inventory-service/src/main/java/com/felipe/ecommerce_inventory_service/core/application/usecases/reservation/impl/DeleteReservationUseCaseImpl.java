package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.DeleteReservationUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;

import java.util.List;
import java.util.UUID;

public class DeleteReservationUseCaseImpl implements DeleteReservationUseCase {
  private final ReservationGateway reservationGateway;

  public DeleteReservationUseCaseImpl(ReservationGateway reservationGateway) {
    this.reservationGateway = reservationGateway;
  }

  @Override
  public void execute(UUID orderId) {
    List<Reservation> allOrderReservations = this.reservationGateway.findAllReservationsByOrderIdAndStatus(orderId, ReservationStatus.RESERVED);
    if (allOrderReservations.isEmpty()) return;
    this.reservationGateway.deleteReservations(allOrderReservations);
  }
}
