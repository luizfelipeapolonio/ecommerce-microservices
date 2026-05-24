package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.SearchProductsResponseDTO;

public interface SearchProductsUseCase {
  SearchProductsResponseDTO execute(String query, int page, int elementsQuantity);
}
