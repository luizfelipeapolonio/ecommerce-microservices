package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import com.felipe.ecommerce_inventory_service.core.application.dtos.reservation.ProductReservationDTO;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;
import com.felipe.utils.Pair;

import java.util.List;
import java.util.UUID;

public interface ReserveProductUseCase {
  Pair<List<Product>, List<Reservation>> execute(UUID orderId, List<ProductReservationDTO> reservationDTOs);
}
