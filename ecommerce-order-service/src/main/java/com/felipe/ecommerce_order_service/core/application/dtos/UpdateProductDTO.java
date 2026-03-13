package com.felipe.ecommerce_order_service.core.application.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record UpdateProductDTO(UUID id, String name, BigDecimal unitPrice) {
}
