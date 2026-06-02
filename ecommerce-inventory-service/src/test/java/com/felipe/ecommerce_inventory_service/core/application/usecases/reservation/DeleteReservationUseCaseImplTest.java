package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl.DeleteReservationUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteReservationUseCaseImplTest {

  @Mock
  private ReservationGateway reservationGateway;

  private DataMock dataMock;
  private DeleteReservationUseCase deleteReservationUseCase;
  private static final UUID ORDER_ID = UUID.fromString("a82da5c2-4b21-4aae-a7ec-93546eb12c75");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.deleteReservationUseCase = new DeleteReservationUseCaseImpl(this.reservationGateway);
  }

  @Test
  @DisplayName("deleteReservationSuccess - Should successfully delete a reservation")
  void deleteReservationSuccess() {
    List<Reservation> reservations = this.dataMock.getReservationsDomain();

    when(this.reservationGateway.findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED))
      .thenReturn(reservations);

    this.deleteReservationUseCase.execute(ORDER_ID);

    verify(this.reservationGateway, times(1)).findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED);
    verify(this.reservationGateway, times(1)).deleteReservations(reservations);
  }

  @Test
  @DisplayName("deleteReservationIsNotExecutedIfReservationsAreNotFound - Should just return if no reservation is found")
  void deleteReservationIsNotExecutedIfReservationsAreNotFound() {
    when(this.reservationGateway.findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED))
      .thenReturn(List.of());

    this.deleteReservationUseCase.execute(ORDER_ID);

    verify(this.reservationGateway, times(1)).findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED);
    verify(this.reservationGateway, never()).deleteReservations(anyList());
  }
}