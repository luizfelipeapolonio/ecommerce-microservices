package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ReservationAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.UnavailableProductException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.ReserveProductUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ReserveProductUseCaseImpl implements ReserveProductUseCase {
  private final ReservationGateway reservationGateway;
  private final ProductGateway productGateway;

  public ReserveProductUseCaseImpl(ReservationGateway reservationGateway, ProductGateway productGateway) {
    this.reservationGateway = reservationGateway;
    this.productGateway = productGateway;
  }

  @Override
  public Reservation execute(UUID productId, UUID orderId, int quantity) {
    Product product = this.productGateway.findProductByIdWithTransactionLock(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + productId + "' não encontrado"));
    Optional<Reservation> existingReservation = this.reservationGateway.findReservationByProductIdAndOrderId(productId, orderId);
    if(existingReservation.isPresent()) {
      throw new ReservationAlreadyExistsException(
        String.format("O produto de id '%s' já foi reservado pelo pedido de id '%s'", productId, orderId)
      );
    }
    List<Reservation> reservations = this.reservationGateway.findReservationsByProductId(productId);

    // throws exception if:
    // quantity to reserve > product stock quantity
    // reservation quantity + quantity to reserve > product stock quantity
    if(!isProductAvailable(product.getQuantity(), reservations, quantity)) {
      throw new UnavailableProductException("O produto de id '" + productId + "' não está disponível no estoque");
    }
    return this.reservationGateway.reserveProduct(productId, orderId, quantity);
  }

  private boolean isProductAvailable(long stockQuantity, List<Reservation> reservations, int quantityToReserve) {
    long reservationQuantity = 0;
    for(Reservation reservation : reservations) {
      reservationQuantity += reservation.getQuantity();
    }
    long totalReservations = reservationQuantity + quantityToReserve;
    return quantityToReserve < stockQuantity || totalReservations <  stockQuantity;
  }
}
