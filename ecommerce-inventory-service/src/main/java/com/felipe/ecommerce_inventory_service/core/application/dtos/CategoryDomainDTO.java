package com.felipe.ecommerce_inventory_service.core.application.dtos;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

record CategoryDomainDTO(Long id, String name, String createdAt, String updatedAt) {
  public CategoryDomainDTO(Category category) {
    this(
      category.getId(),
      category.getName(),
      category.getCreatedAt().toString(),
      category.getUpdatedAt().toString()
    );
  }
}
