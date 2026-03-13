package com.felipe.ecommerce_inventory_service.core.application.dtos.reservation;

import java.util.UUID;

public record ProductReservationDTO(UUID productId, long quantity) {
}
