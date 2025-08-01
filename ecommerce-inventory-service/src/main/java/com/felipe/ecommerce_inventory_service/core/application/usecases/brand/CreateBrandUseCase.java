package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;

public interface CreateBrandUseCase {
  Brand execute(String name, String description);
}
