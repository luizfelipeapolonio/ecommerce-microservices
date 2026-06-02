package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ReservationEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ReservationEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ReservationRepository;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationGatewayImplTest {

  @Mock
  private ReservationRepository reservationRepository;

  @Mock
  private ReservationEntityMapper reservationEntityMapper;

  @InjectMocks
  private ReservationGatewayImpl reservationGateway;

  private DataMock dataMock;
  private static final UUID ORDER_ID = UUID.fromString("a82da5c2-4b21-4aae-a7ec-93546eb12c75");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("reserveProductSuccess - Should successfully reserve a product")
  void reserveProductSuccess() {
    List<ReservationEntity> reservations = List.of(
      this.dataMock.getReservationsEntity().get(0).productId(UUID.fromString("922fc46d-49b7-4150-86c2-f4ce09426636")),
      this.dataMock.getReservationsEntity().get(1).productId(UUID.fromString("17a78691-e2d0-4d39-8a79-a7b4d3ac2ae3"))
    );
    List<ProductReservationDTO> reservationDTOs = List.of(
      new ProductReservationDTO(reservations.get(0).getProductId(), 1),
      new ProductReservationDTO(reservations.get(1).getProductId(), 1)
    );
    ArgumentCaptor<ReservationEntity> captor = ArgumentCaptor.forClass(ReservationEntity.class);

    List<Reservation> reservationsDone = this.reservationGateway.reserveProduct(ORDER_ID, reservationDTOs);

    verify(this.reservationRepository, times(2)).save(captor.capture());
    assertThat(reservationsDone.isEmpty()).isFalse();
    assertThat(captor.getAllValues()).allSatisfy(entity -> {
      assertThat(entity.getStatus()).isEqualTo(ReservationStatus.RESERVED.getText());
      assertThat(entity.getOrderId()).isEqualTo(ORDER_ID);
    });
    assertThat(captor.getAllValues().get(0).getProductId()).isEqualTo(reservationDTOs.get(0).productId());
    assertThat(captor.getAllValues().get(0).getQuantity()).isEqualTo(reservationDTOs.get(0).quantity());
    assertThat(captor.getAllValues().get(1).getProductId()).isEqualTo(reservationDTOs.get(1).productId());
    assertThat(captor.getAllValues().get(1).getQuantity()).isEqualTo(reservationDTOs.get(1).quantity());
    verify(this.reservationEntityMapper, times(2)).toDomain(any());
  }

  @Test
  @DisplayName("findReservationByOrderIdSuccess - Should find a reservation by order id and return an optional of Reservation")
  void findReservationByOrderIdSuccess() {
    Reservation reservationDomain = this.dataMock.getReservationsDomain().getFirst();
    ReservationEntity reservationEntity = this.dataMock.getReservationsEntity().getFirst();

    when(this.reservationRepository.findByOrderId(ORDER_ID)).thenReturn(Optional.of(reservationEntity));
    when(this.reservationEntityMapper.toDomain(reservationEntity)).thenReturn(reservationDomain);

    Optional<Reservation> foundReservation = this.reservationGateway.findReservationByOrderId(ORDER_ID);

    assertThat(foundReservation.isPresent()).isTrue();
    assertThat(foundReservation.get().getId()).isEqualTo(reservationEntity.getId());
    assertThat(foundReservation.get().getOrderId()).isEqualTo(reservationEntity.getOrderId());
    assertThat(foundReservation.get().getQuantity()).isEqualTo(reservationEntity.getQuantity());
    assertThat(foundReservation.get().getStatus()).isEqualTo(reservationEntity.getStatus());
    assertThat(foundReservation.get().getProductId()).isEqualTo(reservationEntity.getProductId());
    assertThat(foundReservation.get().getCreatedAt()).isEqualTo(reservationEntity.getCreatedAt());
    assertThat(foundReservation.get().getUpdatedAt()).isEqualTo(reservationEntity.getUpdatedAt());

    verify(this.reservationRepository, times(1)).findByOrderId(ORDER_ID);
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationEntity);
  }

  @Test
  @DisplayName("findAllReservationsByOrderIdAndStatusSuccess - Should find all reservations by order id and status")
  void findAllReservationsByOrderIdAndStatusSuccess() {
    List<ReservationEntity> reservationsEntity = this.dataMock.getReservationsEntity();
    List<Reservation> reservationsDomain = this.dataMock.getReservationsDomain();

    when(this.reservationRepository.findAllByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED.getText()))
      .thenReturn(reservationsEntity);
    when(this.reservationEntityMapper.toDomain(reservationsEntity.get(0))).thenReturn(reservationsDomain.get(0));
    when(this.reservationEntityMapper.toDomain(reservationsEntity.get(1))).thenReturn(reservationsDomain.get(1));
    when(this.reservationEntityMapper.toDomain(reservationsEntity.get(2))).thenReturn(reservationsDomain.get(2));

    List<Reservation> reservations = this.reservationGateway.findAllReservationsByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED);

    assertThat(reservations.size()).isEqualTo(3);
    assertThat(reservations.get(0)).usingRecursiveComparison().isEqualTo(reservationsEntity.get(0));
    assertThat(reservations.get(1)).usingRecursiveComparison().isEqualTo(reservationsEntity.get(1));
    assertThat(reservations.get(2)).usingRecursiveComparison().isEqualTo(reservationsEntity.get(2));

    verify(this.reservationRepository, times(1)).findAllByOrderIdAndStatus(ORDER_ID, ReservationStatus.RESERVED.getText());
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationsEntity.get(0));
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationsEntity.get(1));
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationsEntity.get(2));
  }

  @Test
  @DisplayName("findReservationsByProductIdsAndStatusSuccess - Should find reservations by product id and status")
  void findReservationsByProductIdsAndStatusSuccess() {
    List<ReservationEntity> reservationsEntity = this.dataMock.getReservationsEntity();
    List<Reservation> reservationDomain = this.dataMock.getReservationsDomain();
    List<UUID> productIds = reservationsEntity.stream().map(ReservationEntity::getProductId).toList();

    when(this.reservationRepository.findByProductIdInAndStatus(productIds, ReservationStatus.RESERVED.getText()))
      .thenReturn(reservationsEntity);
    when(this.reservationEntityMapper.toDomain(reservationsEntity.get(0))).thenReturn(reservationDomain.get(0));
    when(this.reservationEntityMapper.toDomain(reservationsEntity.get(1))).thenReturn(reservationDomain.get(1));
    when(this.reservationEntityMapper.toDomain(reservationsEntity.get(2))).thenReturn(reservationDomain.get(2));

    List<Reservation> reservations = this.reservationGateway.findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED);

    assertThat(reservations).isNotEmpty();
    assertThat(reservations.size()).isEqualTo(3);
    assertThat(reservations.get(0)).usingRecursiveComparison().isEqualTo(reservationsEntity.get(0));
    assertThat(reservations.get(1)).usingRecursiveComparison().isEqualTo(reservationsEntity.get(1));
    assertThat(reservations.get(2)).usingRecursiveComparison().isEqualTo(reservationsEntity.get(2));

    verify(this.reservationRepository, times(1)).findByProductIdInAndStatus(productIds, ReservationStatus.RESERVED.getText());
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationsEntity.get(0));
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationsEntity.get(1));
    verify(this.reservationEntityMapper, times(1)).toDomain(reservationsEntity.get(2));
  }

  @Test
  @DisplayName("updateReservationsSuccess - Should successfully update the reservations")
  void updateReservationsSuccess() {
    List<ReservationEntity> reservationsEntity = this.dataMock.getReservationsEntity();
    List<Reservation> reservationsDomain = this.dataMock.getReservationsDomain();

    when(this.reservationEntityMapper.toEntity(reservationsDomain.get(0))).thenReturn(reservationsEntity.get(0));
    when(this.reservationEntityMapper.toEntity(reservationsDomain.get(1))).thenReturn(reservationsEntity.get(1));
    when(this.reservationEntityMapper.toEntity(reservationsDomain.get(2))).thenReturn(reservationsEntity.get(2));
    when(this.reservationRepository.save(reservationsEntity.get(0))).thenReturn(reservationsEntity.get(0));
    when(this.reservationRepository.save(reservationsEntity.get(1))).thenReturn(reservationsEntity.get(1));
    when(this.reservationRepository.save(reservationsEntity.get(2))).thenReturn(reservationsEntity.get(2));

    this.reservationGateway.updateReservations(reservationsDomain);

    verify(this.reservationEntityMapper, times(1)).toEntity(reservationsDomain.get(0));
    verify(this.reservationEntityMapper, times(1)).toEntity(reservationsDomain.get(1));
    verify(this.reservationEntityMapper, times(1)).toEntity(reservationsDomain.get(2));
    verify(this.reservationRepository, times(1)).save(reservationsEntity.get(0));
    verify(this.reservationRepository, times(1)).save(reservationsEntity.get(1));
    verify(this.reservationRepository, times(1)).save(reservationsEntity.get(2));
  }

  @Test
  @DisplayName("deleteReservationsSuccess - Should successfully delete reservations")
  void deleteReservationsSuccess() {
    List<ReservationEntity> reservationsEntity = this.dataMock.getReservationsEntity();
    List<Reservation> reservationsDomain = this.dataMock.getReservationsDomain();

    when(this.reservationEntityMapper.toEntity(reservationsDomain.get(0))).thenReturn(reservationsEntity.get(0));
    when(this.reservationEntityMapper.toEntity(reservationsDomain.get(1))).thenReturn(reservationsEntity.get(1));
    when(this.reservationEntityMapper.toEntity(reservationsDomain.get(2))).thenReturn(reservationsEntity.get(2));

    this.reservationGateway.deleteReservations(reservationsDomain);

    verify(this.reservationEntityMapper, times(1)).toEntity(reservationsDomain.get(0));
    verify(this.reservationEntityMapper, times(1)).toEntity(reservationsDomain.get(1));
    verify(this.reservationEntityMapper, times(1)).toEntity(reservationsDomain.get(2));
    verify(this.reservationRepository, times(1))
      .deleteAll(assertArg(reservations ->
        assertThat(reservations).usingRecursiveComparison().isEqualTo(reservationsEntity)));
  }
}