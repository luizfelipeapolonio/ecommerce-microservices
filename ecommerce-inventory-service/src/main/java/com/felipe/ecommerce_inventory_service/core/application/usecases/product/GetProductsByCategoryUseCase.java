package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;

public interface GetProductsByCategoryUseCase {
  PageResponseDTO execute(String categoryName, int page, int elementsQuantity);
}
