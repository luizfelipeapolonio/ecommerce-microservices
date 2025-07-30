package com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.DeleteCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {
  private final CategoryGateway categoryGateway;

  public DeleteCategoryUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public Category execute(Long id) {
    Category category = this.categoryGateway.findCategoryById(id)
      .orElseThrow(() -> new DataNotFoundException("Categoria de id '" + id + "' não encontrada"));
    return this.categoryGateway.deleteCategory(category);
  }
}
