package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;

import java.util.UUID;

public interface GetProductByIdUseCase {
  ProductResponseDTO execute(UUID productId);
}
