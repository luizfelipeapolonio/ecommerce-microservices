package com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.category.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetAllCategoriesUseCase;

import java.util.List;

public class GetAllCategoriesUseCaseImpl implements GetAllCategoriesUseCase {
  private final CategoryGateway categoryGateway;

  public GetAllCategoriesUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public List<CategoriesDTO> execute() {
    return this.categoryGateway.getAllCategories();
  }
}
