package com.felipe.ecommerce_inventory_service.core.application.usecases;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

public interface GetCategoryByIdUseCase {
  Category execute(Long id);
}
