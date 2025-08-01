package com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.Optional;

public class CreateSubcategoryUseCaseImpl implements CreateSubcategoryUseCase {
  private final CategoryGateway categoryGateway;

  public CreateSubcategoryUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public Category execute(Long parentCategoryId, String subcategoryName) {
    Optional<Category> existingCategory = this.categoryGateway.findCategoryByName(subcategoryName);
    if(existingCategory.isPresent()) {
      throw new CategoryAlreadyExistsException(subcategoryName);
    }

    Category parentCategory = this.categoryGateway.findCategoryById(parentCategoryId)
      .orElseThrow(() -> new DataNotFoundException("Categoria de id '" + parentCategoryId + "' não encontrada"));

    return this.categoryGateway.createSubcategory(subcategoryName, parentCategory);
  }
}
