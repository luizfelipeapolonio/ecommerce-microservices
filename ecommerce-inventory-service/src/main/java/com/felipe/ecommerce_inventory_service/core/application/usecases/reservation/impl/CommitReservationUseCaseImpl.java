package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.CommitReservationUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommitReservationUseCaseImpl implements CommitReservationUseCase {
  private final ReservationGateway reservationGateway;
  private final ProductGateway productGateway;

  public CommitReservationUseCaseImpl(ReservationGateway reservationGateway, ProductGateway productGateway) {
    this.reservationGateway = reservationGateway;
    this.productGateway = productGateway;
  }

  @Override
  public void execute(UUID orderId) {
    List<Reservation> reservations = this.reservationGateway.findAllReservationsByOrderIdAndStatus(orderId, ReservationStatus.RESERVED);
    if (reservations.isEmpty()) {
      throw new DataNotFoundException("Reservas do pedido de id '" + orderId + "' não encontradas");
    }
    List<UUID> productIds = reservations.stream().map(Reservation::getProductId).toList();
    Map<UUID, Reservation> reservationByProductId = mapReservationByProductId(reservations);

    productIds.forEach(productId -> {
      Reservation reservation = reservationByProductId.get(productId);
      Product product = this.productGateway.findProductById(productId)
        .orElseThrow(() -> new DataNotFoundException("Produto de id '" + productId + "' não encontrado"));

      long newQuantity = product.getQuantity() - reservation.getQuantity();
      UpdateProductDomainDTO updateDTO = new UpdateProductDTO(null, null, null, newQuantity);

      this.productGateway.updateProduct(product, updateDTO);
      reservation.setStatus(ReservationStatus.CONFIRMED);
    });
    List<Reservation> updatedReservations = reservationByProductId.values().stream().toList();
    this.reservationGateway.updateReservations(updatedReservations);
  }

  private Map<UUID, Reservation> mapReservationByProductId(List<Reservation> reservations) {
    return reservations.stream().collect(Collectors.toMap(
      Reservation::getProductId,
      Function.identity()
    ));
  }

  private record UpdateProductDTO(String name, String description, String unitPrice, Long quantity) implements UpdateProductDomainDTO {}
}
