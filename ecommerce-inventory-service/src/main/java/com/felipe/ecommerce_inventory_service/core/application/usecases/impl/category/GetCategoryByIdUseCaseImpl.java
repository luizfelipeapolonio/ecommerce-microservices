package com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

public class GetCategoryByIdUseCaseImpl implements GetCategoryByIdUseCase {
  private final CategoryGateway categoryGateway;

  public GetCategoryByIdUseCaseImpl(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Override
  public Category execute(Long id) {
    return this.categoryGateway.findCategoryById(id)
      .orElseThrow(() -> new DataNotFoundException("Categoria de id '" + id + "' não encontrada"));
  }
}
