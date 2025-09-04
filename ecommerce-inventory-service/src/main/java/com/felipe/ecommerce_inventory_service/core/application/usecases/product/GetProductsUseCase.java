package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;

public interface GetProductsUseCase {
  PageResponseDTO execute(String category, String brand, String model, int page, int elementsQuantity);
}
