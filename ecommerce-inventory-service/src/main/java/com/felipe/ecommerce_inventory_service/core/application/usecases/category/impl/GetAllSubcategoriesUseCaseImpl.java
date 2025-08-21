package com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl;

import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetAllSubcategoriesUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.List;

public class GetAllSubcategoriesUseCaseImpl implements GetAllSubcategoriesUseCase {
  private final CategoryGateway categoryGateway;

  public GetAllSubcategoriesUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public List<Category> execute() {
    return this.categoryGateway.getAllSubcategories();
  }
}
