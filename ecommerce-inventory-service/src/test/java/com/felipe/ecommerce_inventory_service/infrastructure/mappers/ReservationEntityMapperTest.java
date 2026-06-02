package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ReservationEntity;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReservationEntityMapperTest {

  @Spy
  private ReservationEntityMapper reservationEntityMapper;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertEntityToDomain - Should successfully convert a ReservationEntity to Reservation")
  void convertEntityToDomain() {
    ReservationEntity entity = this.dataMock.getReservationsEntity().getFirst();

    Reservation convertedReservation = this.reservationEntityMapper.toDomain(entity);

    assertThat(convertedReservation.getId()).isEqualTo(entity.getId());
    assertThat(convertedReservation.getProductId()).isEqualTo(entity.getProductId());
    assertThat(convertedReservation.getOrderId()).isEqualTo(entity.getOrderId());
    assertThat(convertedReservation.getQuantity()).isEqualTo(entity.getQuantity());
    assertThat(convertedReservation.getStatus()).isEqualTo(entity.getStatus());
    assertThat(convertedReservation.getCreatedAt()).isEqualTo(entity.getCreatedAt());
    assertThat(convertedReservation.getUpdatedAt()).isEqualTo(entity.getUpdatedAt());

    verify(this.reservationEntityMapper, times(1)).toDomain(entity);
  }

  @Test
  @DisplayName("convertDomainToEntity - Should successfully convert ReservationEntity to Reservation")
  void convertDomainToEntity() {
    Reservation domain = this.dataMock.getReservationsDomain().getFirst();

    ReservationEntity convertedReservation = this.reservationEntityMapper.toEntity(domain);

    assertThat(convertedReservation.getId()).isEqualTo(domain.getId());
    assertThat(convertedReservation.getProductId()).isEqualTo(domain.getProductId());
    assertThat(convertedReservation.getOrderId()).isEqualTo(domain.getOrderId());
    assertThat(convertedReservation.getQuantity()).isEqualTo(domain.getQuantity());
    assertThat(convertedReservation.getStatus()).isEqualTo(domain.getStatus());
    assertThat(convertedReservation.getCreatedAt()).isEqualTo(domain.getCreatedAt());
    assertThat(convertedReservation.getUpdatedAt()).isEqualTo(domain.getUpdatedAt());

    verify(this.reservationEntityMapper, times(1)).toEntity(domain);
  }
}