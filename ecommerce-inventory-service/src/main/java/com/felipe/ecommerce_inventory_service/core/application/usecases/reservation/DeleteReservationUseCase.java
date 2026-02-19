package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import java.util.UUID;

public interface DeleteReservationUseCase {
  void execute(UUID productId, UUID orderId);
}
