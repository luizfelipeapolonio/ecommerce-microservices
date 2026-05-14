package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ReservationAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.UnavailableProductException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ReservationGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.reservation.ReserveProductUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import com.felipe.utils.Pair;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ReserveProductUseCaseImpl implements ReserveProductUseCase {
  private final ReservationGateway reservationGateway;
  private final ProductGateway productGateway;

  public ReserveProductUseCaseImpl(ReservationGateway reservationGateway, ProductGateway productGateway) {
    this.reservationGateway = reservationGateway;
    this.productGateway = productGateway;
  }

  @Override
  public Pair<List<Product>, List<Reservation>> execute(UUID orderId, List<ProductReservationDTO> reservationDTOs) {
    List<Product> products = reservationDTOs.stream()
      .map(reservation -> {
          Product product = this.productGateway.findProductByIdWithTransactionLock(reservation.productId())
            .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + reservation.productId() + "' não encontrado"));

          if (product.getQuantity() == 0) {
            throw new UnavailableProductException("O produto de id '" + product.getId() + "' não está disponível no estoque");
          }
          return product;
        }
      )
      .toList();

    Optional<Reservation> existingReservation = this.reservationGateway.findReservationByOrderId(orderId);
    if (existingReservation.isPresent()) {
      throw new ReservationAlreadyExistsException(
        String.format("A reserva dos produtos do pedido de id '%s' já existe", orderId)
      );
    }
    List<UUID> productIds = products.stream().map(Product::getId).toList();
    List<Reservation> reservations = this.reservationGateway.findReservationsByProductIdsAndStatus(productIds, ReservationStatus.RESERVED);

    // throws exception if:
    // quantity to reserve > product stock quantity
    // reserved quantity + quantity to reserve > product stock quantity
    checkIfProductsAreAvailable(products, reservations, reservationDTOs);
    List<Reservation> reservationsDone = this.reservationGateway.reserveProduct(orderId, reservationDTOs);
    return new Pair<>(products, reservationsDone);
  }

  private void checkIfProductsAreAvailable(List<Product> products, List<Reservation> reservations, List<ProductReservationDTO> dtos) {
    Map<UUID, Long> reservationsQuantity = dtos.stream()
      .collect(Collectors.toMap(
        ProductReservationDTO::productId,
        ProductReservationDTO::quantity
      ));

    for (Product product : products) {
      long stockQuantity = product.getQuantity();
      long quantityToReserve = reservationsQuantity.get(product.getId());

      if (!isProductAvailable(stockQuantity, reservations, quantityToReserve)) {
        throw new UnavailableProductException("O produto de id '" + product.getId() + "' não está disponível no estoque");
      }
    }
  }

  private boolean isProductAvailable(long stockQuantity, List<Reservation> reservations, long quantityToReserve) {
    long reservedQuantity = 0;
    for (Reservation reservation : reservations) {
      reservedQuantity += reservation.getQuantity();
    }
    long totalReservations = reservedQuantity + quantityToReserve;
    return quantityToReserve <= stockQuantity && totalReservations <= stockQuantity;
  }
}
