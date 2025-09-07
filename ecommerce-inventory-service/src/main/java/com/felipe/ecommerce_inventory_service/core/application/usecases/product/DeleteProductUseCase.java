package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public interface DeleteProductUseCase {
  Product execute(UUID productId);
}
