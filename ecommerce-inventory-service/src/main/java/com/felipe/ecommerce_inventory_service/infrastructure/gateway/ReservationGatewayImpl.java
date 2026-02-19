package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

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
  public Reservation reserveProduct(UUID productId, UUID orderId, int quantity) {
    ReservationEntity newReservation = new ReservationEntity()
      .productId(productId)
      .orderId(orderId)
      .quantity(quantity)
      .status(ReservationStatus.RESERVED);
    ReservationEntity savedReservation = this.reservationRepository.save(newReservation);
    return this.reservationEntityMapper.toDomain(savedReservation);
  }

  @Override
  public List<Reservation> findReservationsByProductId(UUID productId) {
    return this.reservationRepository.findAllByProductId(productId)
      .stream()
      .map(this.reservationEntityMapper::toDomain)
      .toList();
  }

  @Override
  public Optional<Reservation> findReservationByProductIdAndOrderId(UUID productId, UUID orderId) {
    return this.reservationRepository.findByProductIdAndOrderId(productId, orderId).map(this.reservationEntityMapper::toDomain);
  }

  @Override
  public void deleteReservation(Long reservationId) {
    this.reservationRepository.deleteById(reservationId);
  }
}
