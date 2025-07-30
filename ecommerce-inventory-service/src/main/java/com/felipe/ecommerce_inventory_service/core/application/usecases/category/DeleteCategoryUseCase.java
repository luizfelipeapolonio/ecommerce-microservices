package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

public interface DeleteCategoryUseCase {
  Category execute(Long id);
}
