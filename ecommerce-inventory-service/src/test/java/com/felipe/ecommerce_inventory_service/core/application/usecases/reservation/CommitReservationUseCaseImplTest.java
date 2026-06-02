package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl.CommitReservationUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommitReservationUseCaseImplTest {

  @Mock
  private ReservationGateway reservationGateway;

  @Mock
  private ProductGateway productGateway;

  private DataMock dataMock;
  private CommitReservationUseCase commitReservationUseCase;
  private List<Reservation> reservations;
  private static final UUID ORDER_ID = UUID.fromString("a82da5c2-4b21-4aae-a7ec-93546eb12c75");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.commitReservationUseCase = new CommitReservationUseCaseImpl(this.reservationGateway, this.productGateway);
    this.reservations = List.of(
      this.dataMock.getReservationsDomain().get(0).productId(UUID.fromString("922fc46d-49b7-4150-86c2-f4ce09426636")),
      this.dataMock.getReservationsDomain().get(1).productId(UUID.fromString("17a78691-e2d0-4d39-8a79-a7b4d3ac2ae3")),
      this.dataMock.getReservationsDomain().get(2).productId(UUID.fromString("78645492-d2d6-4e1d-a1a4-007ee4eb7824"))
    );
  }

  @Test
  @DisplayName("commitReservationSuccess - Should successfully commit a reservation")
  void commitReservationSuccess() {
    List<Product> products = this.dataMock.getProductsDomain();

    when(this.reservationGateway.findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED))
      .thenReturn(this.reservations);
    when(this.productGateway.findProductById(this.reservations.get(0).getProductId()))
      .thenReturn(Optional.of(products.get(0)));
    when(this.productGateway.findProductById(this.reservations.get(1).getProductId()))
      .thenReturn(Optional.of(products.get(1)));
    when(this.productGateway.findProductById(this.reservations.get(2).getProductId()))
      .thenReturn(Optional.of(products.get(2)));

    this.commitReservationUseCase.execute(ORDER_ID);

    verify(this.reservationGateway, times(1)).findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED);
    verify(this.productGateway, times(1)).findProductById(this.reservations.get(0).getProductId());
    verify(this.productGateway, times(1)).findProductById(this.reservations.get(1).getProductId());
    verify(this.productGateway, times(1)).findProductById(this.reservations.get(2).getProductId());
    verify(this.productGateway, times(1))
      .updateProduct(eq(products.get(0)), assertArg(updateDTO -> {
        long newQuantity = products.getFirst().getQuantity() - this.reservations.getFirst().getQuantity();
        assertThat(updateDTO.quantity()).isEqualTo(newQuantity);
      }));
    verify(this.productGateway, times(1))
      .updateProduct(eq(products.get(1)), assertArg(updateDTO -> {
        long newQuantity = products.get(1).getQuantity() - this.reservations.get(1).getQuantity();
        assertThat(updateDTO.quantity()).isEqualTo(newQuantity);
      }));
    verify(this.productGateway, times(1))
      .updateProduct(eq(products.get(2)), assertArg(updateDTO -> {
        long newQuantity = products.get(2).getQuantity() - this.reservations.get(2).getQuantity();
        assertThat(updateDTO.quantity()).isEqualTo(newQuantity);
      }));
    verify(this.reservationGateway, times(1))
      .updateReservations(assertArg(updatedReservations ->
        assertThat(updatedReservations)
          .allMatch(reservation -> reservation.getStatus().equals(ReservationStatus.CONFIRMED.getText()))));
  }

  @Test
  @DisplayName("commitReservationFailsByReservationsNotFound - Should throw a DataNotFoundException if reservations are not found")
  void commitReservationFailsByReservationsNotFound() {
    when(this.reservationGateway.findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED))
      .thenReturn(List.of());

    Exception thrown = catchException(() -> this.commitReservationUseCase.execute(ORDER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Reservas do pedido de id '%s' não encontradas", ORDER_ID);

    verify(this.reservationGateway, times(1)).findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED);
    verify(this.productGateway, never()).findProductById(any(UUID.class));
    verify(this.productGateway, never()).updateProduct(any(Product.class), any(UpdateProductDomainDTO.class));
    verify(this.reservationGateway, never()).updateReservations(anyList());
  }

  @Test
  @DisplayName("commitReservationFailsByProductNotFound - Should throw a DataNotFoundException if a product is not found")
  void commitReservationFailsByProductNotFound() {
    when(this.reservationGateway.findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED))
      .thenReturn(this.reservations);
    when(this.productGateway.findProductById(this.reservations.getFirst().getProductId()))
      .thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.commitReservationUseCase.execute(ORDER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id '%s' não encontrado", this.reservations.getFirst().getProductId());

    verify(this.reservationGateway, times(1)).findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED);
    verify(this.productGateway, times(1)).findProductById(any(UUID.class));
    verify(this.productGateway, times(1)).findProductById(this.reservations.getFirst().getProductId());
    verify(this.productGateway, never()).updateProduct(any(Product.class), any(UpdateProductDomainDTO.class));
    verify(this.reservationGateway, never()).updateReservations(anyList());
  }
}