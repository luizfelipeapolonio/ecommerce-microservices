package com.felipe.ecommerce_order_service.core.application.dtos;

import java.util.UUID;

public interface CreateOrderProductDTO {
  UUID id();
  long quantity();
}
