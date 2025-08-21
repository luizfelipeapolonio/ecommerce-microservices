package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;

public record UpdateProductResponseDTO(String id,
                                       String name,
                                       String description,
                                       String unitPrice,
                                       Long quantity,
                                       String createdAt,
                                       String updatedAt,
                                       CategoryDTO category,
                                       BrandDTO brand,
                                       ModelDTO model) {
  public UpdateProductResponseDTO(Product product) {
    this(
      product.getId().toString(),
      product.getName(),
      product.getDescription(),
      product.getUnitPrice().toString(),
      product.getQuantity(),
      product.getCreatedAt().toString(),
      product.getUpdatedAt().toString(),
      new CategoryDTO(product.getCategory()),
      new BrandDTO(product.getBrand()),
      new ModelDTO(product.getModel())
    );
  }
}
