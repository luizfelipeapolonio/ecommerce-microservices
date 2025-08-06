package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.domain.Model;

public interface CreateModelUseCase {
  Model execute(String name, String description, Long brandId);
}
