package com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.UpdateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.Optional;

public class UpdateCategoryUseCaseImpl implements UpdateCategoryUseCase {
  private final CategoryGateway categoryGateway;

  public UpdateCategoryUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public Category execute(Long id, String updatedName) {
    Category category = this.categoryGateway.findCategoryById(id)
      .orElseThrow(() -> new DataNotFoundException("Categoria de id '" + id + "' não encontrada"));

    Optional<Category> existingCategory = this.categoryGateway.findCategoryByName(updatedName);
    if(existingCategory.isPresent()) {
      throw new CategoryAlreadyExistsException(updatedName);
    }

    return this.categoryGateway.updateCategory(category, updatedName);
  }
}
