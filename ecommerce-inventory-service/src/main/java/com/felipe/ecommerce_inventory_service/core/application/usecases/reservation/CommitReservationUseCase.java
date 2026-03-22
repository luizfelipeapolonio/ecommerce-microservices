package com.felipe.ecommerce_inventory_service.core.application.usecases.reservation;

import java.util.UUID;

public interface CommitReservationUseCase {
  void execute(UUID orderId);
}
