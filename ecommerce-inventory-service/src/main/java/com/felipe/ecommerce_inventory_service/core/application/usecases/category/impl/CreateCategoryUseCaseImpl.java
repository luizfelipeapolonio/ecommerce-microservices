package com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.Optional;

public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {
  private final CategoryGateway categoryGateway;

  public CreateCategoryUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public Category execute(String name) {
    Optional<Category> existingCategory = this.categoryGateway.findCategoryByName(name);
    if(existingCategory.isPresent()) {
      throw new CategoryAlreadyExistsException(name);
    }
    return this.categoryGateway.createCategory(name);
  }
}
