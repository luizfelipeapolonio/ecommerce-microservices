package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;

import java.util.List;

public record ProductDTO(String id,
                         String name,
                         String description,
                         String unitPrice,
                         long quantity,
                         boolean withDiscount,
                         String discountType,
                         String discountValue,
                         String createdAt,
                         String updatedAt,
                         CategoryDTO category,
                         BrandDTO brand,
                         ModelDTO model,
                         List<ImageFileDTO> images) implements ProductResponseDTO {
  public ProductDTO(Product product, List<ImageFileDTO> images) {
    this(
      product.getId().toString(),
      product.getName(),
      product.getDescription(),
      product.getUnitPrice().toString(),
      product.getQuantity(),
      product.isItWithDiscount(),
      product.getDiscountType() == null ? null : product.getDiscountType(),
      product.getDiscountValue() == null ? null : product.getDiscountValue(),
      product.getCreatedAt().toString(),
      product.getUpdatedAt().toString(),
      new CategoryDTO(product.getCategory()),
      new BrandDTO(product.getBrand()),
      new ModelDTO(product.getModel()),
      images
    );
  }
}
