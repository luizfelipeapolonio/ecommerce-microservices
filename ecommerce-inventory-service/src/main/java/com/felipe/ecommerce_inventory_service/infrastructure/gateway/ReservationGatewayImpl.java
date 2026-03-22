package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ReservationEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ReservationEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ReservationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class ReservationGatewayImpl implements ReservationGateway {
  private final ReservationRepository reservationRepository;
  private final ReservationEntityMapper reservationEntityMapper;

  public ReservationGatewayImpl(ReservationRepository reservationRepository, ReservationEntityMapper reservationEntityMapper) {
    this.reservationRepository = reservationRepository;
    this.reservationEntityMapper = reservationEntityMapper;
  }

  @Override
  public List<Reservation> reserveProduct(UUID orderId, List<ProductReservationDTO> reservationDTOs) {
    List<ReservationEntity> reservations = reservationDTOs
      .stream()
      .map(reservationDTO -> {
        ReservationEntity newReservation = new ReservationEntity()
          .productId(reservationDTO.productId())
          .orderId(orderId)
          .quantity(reservationDTO.quantity())
          .status(ReservationStatus.RESERVED);
        return this.reservationRepository.save(newReservation);
      })
      .toList();
    return reservations.stream().map(this.reservationEntityMapper::toDomain).toList();
  }

  @Override
  public Optional<Reservation> findReservationByOrderId(UUID orderId) {
    return this.reservationRepository.findByOrderId(orderId).map(this.reservationEntityMapper::toDomain);
  }

  @Override
  public List<Reservation> findAllReservationsByOrderIdAndStatus(UUID orderId, ReservationStatus status) {
    return this.reservationRepository.findAllByOrderIdAndStatus(orderId, status.getText())
      .stream()
      .map(this.reservationEntityMapper::toDomain)
      .toList();
  }

  @Override
  public List<Reservation> findReservationsByProductIds(List<UUID> productIds) {
    return this.reservationRepository.findByProductIdIn(productIds)
      .stream()
      .map(this.reservationEntityMapper::toDomain)
      .toList();
  }

  @Override
  public void updateReservations(List<Reservation> reservations) {
    reservations.forEach(reservation -> {
      ReservationEntity entity = this.reservationEntityMapper.toEntity(reservation);
      this.reservationRepository.save(entity);
    });
  }

  @Override
  public void deleteReservations(List<Reservation> reservations) {
    List<ReservationEntity> entities = reservations.stream().map(this.reservationEntityMapper::toEntity).toList();
    this.reservationRepository.deleteAll(entities);
  }
}
