package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.domain.Model;

public interface GetModelByIdUseCase {
  Model execute(Long id);
}
