package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;

public interface GetProductsByModelUseCase {
  PageResponseDTO execute(String modelName, String brandName, int page, int elementsQuantity);
}
