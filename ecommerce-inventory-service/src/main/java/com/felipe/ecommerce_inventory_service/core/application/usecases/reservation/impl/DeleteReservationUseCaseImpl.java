package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.DeleteReservationUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;

import java.util.UUID;

public class DeleteReservationUseCaseImpl implements DeleteReservationUseCase {
  private final ReservationGateway reservationGateway;

  public DeleteReservationUseCaseImpl(ReservationGateway reservationGateway) {
    this.reservationGateway = reservationGateway;
  }

  @Override
  public void execute(UUID productId, UUID orderId) {
    Reservation reservation = this.reservationGateway.findReservationByProductIdAndOrderId(productId, orderId)
      .orElseThrow(() -> new DataNotFoundException(
        "Reserva do produto de id '" + productId + "' e pedido de id '" + orderId + "' não encontrada"
      ));
    this.reservationGateway.deleteReservation(reservation.getId());
  }
}
