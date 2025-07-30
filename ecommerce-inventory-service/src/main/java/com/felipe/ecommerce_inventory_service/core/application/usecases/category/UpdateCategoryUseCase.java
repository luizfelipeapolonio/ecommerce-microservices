package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

public interface UpdateCategoryUseCase {
  Category execute(Long id, String updatedName);
}
