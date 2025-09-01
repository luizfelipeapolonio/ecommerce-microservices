package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;

public interface GetProductsByBrandUseCase {
  PageResponseDTO execute(String brandName, int page, int elementsQuantity);
}
