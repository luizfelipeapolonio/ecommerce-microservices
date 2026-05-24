package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.SearchProductsResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.SearchProductsUseCase;

public class SearchProductsUseCaseImpl implements SearchProductsUseCase {
  private final ProductGateway productGateway;

  public SearchProductsUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public SearchProductsResponseDTO execute(String query, int page, int elementsQuantity) {
    return this.productGateway.searchProducts(query, page, elementsQuantity);
  }
}
