package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.Optional;

public class GetProductsByCategoryUseCaseImpl implements GetProductsByCategoryUseCase {
  private final ProductGateway productGateway;
  private final CategoryGateway categoryGateway;

  public GetProductsByCategoryUseCaseImpl(ProductGateway productGateway, CategoryGateway categoryGateway) {
    this.productGateway = productGateway;
    this.categoryGateway = categoryGateway;
  }

  @Override
  public PageResponseDTO execute(String categoryName, int page, int elementsQuantity) {
    Optional<Category> existingCategory = this.categoryGateway.findCategoryByName(categoryName);
    if(existingCategory.isEmpty()) {
      throw new DataNotFoundException("Categoria '" + categoryName + "' não encontrada");
    }
    return this.productGateway.getProductsByCategory(categoryName, page, elementsQuantity);
  }
}
