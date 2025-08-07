package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.domain.Model;

public interface DeleteModelUseCase {
  Model execute(Long id);
}
