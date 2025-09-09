package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductInStockDTO;

import java.util.UUID;

public interface CheckIfProductIsInStockUseCase {
  ProductInStockDTO execute(UUID productId);
}
