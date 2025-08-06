package com.felipe.ecommerce_inventory_service.infrastructure.dtos.model;

import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;

public record ModelDTO(Long id, String name, String description, String createdAt, String updatedAt, BrandDTO brand) {
  public ModelDTO(Model model) {
    this(
      model.getId(),
      model.getName(),
      model.getDescription(),
      model.getCreatedAt().toString(),
      model.getUpdatedAt().toString(),
      new BrandDTO(model.getBrand())
    );
  }
}
