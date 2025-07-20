package com.felipe.ecommerce_inventory_service.infrastructure.dtos.category;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

public record CategoryDTO(Long id, String name, String createdAt, String updatedAt, CategoryDTO parentCategory) {
  public CategoryDTO(Category category) {
    this(
      category.getId(),
      category.getName(),
      category.getCreatedAt().toString(),
      category.getUpdatedAt().toString(),
      category.getParentCategory() == null ? null : new CategoryDTO(category.getParentCategory())
    );
  }
}