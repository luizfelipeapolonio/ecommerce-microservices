package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.domain.Model;

public interface UpdateModelUseCase {
  Model execute(Long id, String name, String description);
}
