package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;

public interface UpdateBrandUseCase {
  Brand execute(Long id, String name, String description);
}
