package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ReservationAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.UnavailableProductException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl.ReserveProductUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import com.felipe.utils.Pair;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReserveProductUseCaseImplTest {

  @Mock
  private ReservationGateway reservationGateway;

  @Mock
  private ProductGateway productGateway;

  private ReserveProductUseCase reserveProductUseCase;
  private DataMock dataMock;
  private List<ProductReservationDTO> reservationDTOs;
  private static final UUID ORDER_ID = UUID.fromString("a82da5c2-4b21-4aae-a7ec-93546eb12c75");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.reserveProductUseCase = new ReserveProductUseCaseImpl(this.reservationGateway, this.productGateway);
    this.reservationDTOs = List.of(
      new ProductReservationDTO(
      UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"),
      1
      ),
      new ProductReservationDTO(
        UUID.fromString("8570948d-ffc8-4239-a1af-75681b31749c"),
        1
      ),
      new ProductReservationDTO(
        UUID.fromString("205aafa7-f5e1-409c-8707-1d673d254374"),
        1
      )
    );
  }

  @Test
  @DisplayName("reserveProductSuccess - Should successfully reserve a product and return it")
  void reserveProductSuccess() {
    List<Product> products = this.dataMock.getProductsDomain();
    List<Reservation> reservations = this.dataMock.getReservationsDomain();
    List<UUID> productIds = products.stream().map(Product::getId).toList();

    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId()))
      .thenReturn(Optional.of(products.get(0)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId()))
      .thenReturn(Optional.of(products.get(1)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId()))
      .thenReturn(Optional.of(products.get(2)));
    when(this.reservationGateway.findReservationByOrderId(ORDER_ID)).thenReturn(Optional.empty());
    when(this.reservationGateway.findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED))
      .thenReturn(reservations);
    when(this.reservationGateway.reserveProduct(ORDER_ID, this.reservationDTOs))
      .thenReturn(reservations);

    Pair<List<Product>, List<Reservation>> reservationsDone = this.reserveProductUseCase.execute(ORDER_ID, this.reservationDTOs);

    assertThat(reservationsDone.t1().isEmpty()).isFalse();
    assertThat(reservationsDone.t2().isEmpty()).isFalse();
    assertThat(reservationsDone.t1().get(0)).usingRecursiveComparison().isEqualTo(products.get(0));
    assertThat(reservationsDone.t1().get(1)).usingRecursiveComparison().isEqualTo(products.get(1));
    assertThat(reservationsDone.t1().get(2)).usingRecursiveComparison().isEqualTo(products.get(2));
    assertThat(reservationsDone.t2().get(0)).usingRecursiveComparison().isEqualTo(reservations.get(0));
    assertThat(reservationsDone.t2().get(1)).usingRecursiveComparison().isEqualTo(reservations.get(1));
    assertThat(reservationsDone.t2().get(2)).usingRecursiveComparison().isEqualTo(reservations.get(2));

    verify(this.productGateway, times(3)).findProductByIdWithTransactionLock(any(UUID.class));
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId());
    verify(this.reservationGateway, times(1)).findReservationByOrderId(ORDER_ID);
    verify(this.reservationGateway, times(1)).findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED);
    verify(this.reservationGateway, times(1)).reserveProduct(ORDER_ID, this.reservationDTOs);
  }

  @Test
  @DisplayName("reserveProductFailsByProductNotFound - Should throw a DataNotFoundException if product is not found")
  void reserveProductFailsByProductNotFound() {
    UUID productId = this.reservationDTOs.getFirst().productId();

    when(this.productGateway.findProductByIdWithTransactionLock(productId))
      .thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.reserveProductUseCase.execute(ORDER_ID, this.reservationDTOs));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id: '%s' não encontrado", productId);

    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(productId);
    verify(this.reservationGateway, never()).findReservationByOrderId(any(UUID.class));
    verify(this.reservationGateway, never()).findReservationsByProductIdsAndStatus(anyList(), any(ReservationStatus.class));
    verify(this.reservationGateway, never()).reserveProduct(any(UUID.class), anyList());
  }

  @Test
  @DisplayName("reserveProductFailsByProductQuantityZero - Should throw an UnavailableProductException is equal to zero")
  void reserveProductFailsByProductQuantityZero() {
    Product product = Product.mutate(this.dataMock.getProductsDomain().getFirst())
      .quantity(0)
      .build();

    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.getFirst().productId()))
      .thenReturn(Optional.of(product));

    Exception thrown = catchException(() -> this.reserveProductUseCase.execute(ORDER_ID, this.reservationDTOs));

    assertThat(thrown)
      .isExactlyInstanceOf(UnavailableProductException.class)
      .hasMessage("O produto de id '%s' não está disponível no estoque", product.getId());

    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.getFirst().productId());
    verify(this.reservationGateway, never()).findReservationByOrderId(any(UUID.class));
    verify(this.reservationGateway, never()).findReservationsByProductIdsAndStatus(anyList(), any(ReservationStatus.class));
    verify(this.reservationGateway, never()).reserveProduct(any(UUID.class), anyList());
  }

  @Test
  @DisplayName("reserveProductFailsByReservationAlreadyExists - Should throw a ReservationAlreadyExistsException if reservation already exists")
  void reserveProductFailsByReservationAlreadyExists() {
    List<Product> products = this.dataMock.getProductsDomain();
    Reservation reservation = this.dataMock.getReservationsDomain().getFirst();

    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId()))
      .thenReturn(Optional.of(products.get(0)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId()))
      .thenReturn(Optional.of(products.get(1)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId()))
      .thenReturn(Optional.of(products.get(2)));
    when(this.reservationGateway.findReservationByOrderId(ORDER_ID)).thenReturn(Optional.of(reservation));

    Exception thrown = catchException(() -> this.reserveProductUseCase.execute(ORDER_ID, this.reservationDTOs));

    assertThat(thrown)
      .isExactlyInstanceOf(ReservationAlreadyExistsException.class)
      .hasMessage("A reserva dos produtos do pedido de id '%s' já existe", ORDER_ID);

    verify(this.productGateway, times(3)).findProductByIdWithTransactionLock(any(UUID.class));
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId());
    verify(this.reservationGateway, times(1)).findReservationByOrderId(ORDER_ID);
    verify(this.reservationGateway, never()).findReservationsByProductIdsAndStatus(anyList(), any(ReservationStatus.class));
    verify(this.reservationGateway, never()).reserveProduct(any(UUID.class), anyList());
  }

  @Test
  @DisplayName(
    "reserveProductFailsByQuantityToReserveGreaterThanStockQuantity - Should throw an UnavailableProductException " +
    "if quantity to reserve is greater than stock quantity"
  )
  void reserveProductFailsByQuantityToReserveGreaterThanStockQuantity() {
    List<Product> products = this.dataMock.getProductsDomain();
    Reservation invalidReservation = this.dataMock.getReservationsDomain().get(1)
      .quantity(51); // greater than stock quantity (50)
    List<Reservation> reservations = List.of(
      this.dataMock.getReservationsDomain().get(0),
      invalidReservation,
      this.dataMock.getReservationsDomain().get(2)
    );
    List<UUID> productIds = products.stream().map(Product::getId).toList();

    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId()))
      .thenReturn(Optional.of(products.get(0)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId()))
      .thenReturn(Optional.of(products.get(1)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId()))
      .thenReturn(Optional.of(products.get(2)));
    when(this.reservationGateway.findReservationByOrderId(ORDER_ID)).thenReturn(Optional.empty());
    when(this.reservationGateway.findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED))
      .thenReturn(reservations);

    Exception thrown = catchException(() -> this.reserveProductUseCase.execute(ORDER_ID, this.reservationDTOs));

    assertThat(thrown)
      .isExactlyInstanceOf(UnavailableProductException.class)
      .hasMessage("O produto de id '%s' não está disponível no estoque", invalidReservation.getProductId());

    verify(this.productGateway, times(3)).findProductByIdWithTransactionLock(any(UUID.class));
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId());
    verify(this.reservationGateway, times(1)).findReservationByOrderId(ORDER_ID);
    verify(this.reservationGateway, times(1)).findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED);
    verify(this.reservationGateway, never()).reserveProduct(any(UUID.class), anyList());
  }

  @Test
  @DisplayName(
    "reserveProductFailsByQuantityToReserveEqualsToStockQuantity - Should throw an UnavailableProductException " +
    " if quantity to reserve is equal to stock quantity"
  )
  void reserveProductFailsByQuantityToReserveEqualsToStockQuantity() {
    List<Product> products = this.dataMock.getProductsDomain();
    Reservation invalidReservation = this.dataMock.getReservationsDomain().get(1)
      .quantity(50); // equals to stock quantity (50)
    List<Reservation> reservations = List.of(
      this.dataMock.getReservationsDomain().get(0),
      invalidReservation,
      this.dataMock.getReservationsDomain().get(2)
    );
    List<UUID> productIds = products.stream().map(Product::getId).toList();

    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId()))
      .thenReturn(Optional.of(products.get(0)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId()))
      .thenReturn(Optional.of(products.get(1)));
    when(this.productGateway.findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId()))
      .thenReturn(Optional.of(products.get(2)));
    when(this.reservationGateway.findReservationByOrderId(ORDER_ID)).thenReturn(Optional.empty());
    when(this.reservationGateway.findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED))
      .thenReturn(reservations);

    Exception thrown = catchException(() -> this.reserveProductUseCase.execute(ORDER_ID, this.reservationDTOs));

    assertThat(thrown)
      .isExactlyInstanceOf(UnavailableProductException.class)
      .hasMessage("O produto de id '%s' não está disponível no estoque", invalidReservation.getProductId());

    verify(this.productGateway, times(3)).findProductByIdWithTransactionLock(any(UUID.class));
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(0).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(1).productId());
    verify(this.productGateway, times(1)).findProductByIdWithTransactionLock(this.reservationDTOs.get(2).productId());
    verify(this.reservationGateway, times(1)).findReservationByOrderId(ORDER_ID);
    verify(this.reservationGateway, times(1)).findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED);
    verify(this.reservationGateway, never()).reserveProduct(any(UUID.class), anyList());
  }
}