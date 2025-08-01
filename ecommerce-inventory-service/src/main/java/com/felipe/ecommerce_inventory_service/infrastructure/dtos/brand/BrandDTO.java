package com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;

public record BrandDTO(Long id, String name, String description, String createdAt, String updatedAt) {
  public BrandDTO(Brand brand) {
    this(
      brand.getId(),
      brand.getName(),
      brand.getDescription(),
      brand.getCreatedAt().toString(),
      brand.getUpdatedAt().toString()
    );
  }
}
