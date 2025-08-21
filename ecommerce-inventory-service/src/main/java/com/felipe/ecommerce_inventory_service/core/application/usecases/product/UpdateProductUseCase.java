package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public interface UpdateProductUseCase {
  Product execute(UUID productId, UpdateProductDomainDTO productDTO);
}
