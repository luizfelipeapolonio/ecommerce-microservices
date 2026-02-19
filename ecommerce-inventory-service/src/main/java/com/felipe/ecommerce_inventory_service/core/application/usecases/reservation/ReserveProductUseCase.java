package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import com.felipe.ecommerce_inventory_service.core.domain.reservation.Reservation;

import java.util.UUID;

public interface ReserveProductUseCase {
  Reservation execute(UUID  productId, UUID orderId, int quantity);
}
