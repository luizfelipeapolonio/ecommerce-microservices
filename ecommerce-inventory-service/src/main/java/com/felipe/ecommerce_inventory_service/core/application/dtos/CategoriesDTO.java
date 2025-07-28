package com.felipe.ecommerce_inventory_service.core.application.dtos;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.List;

public record CategoriesDTO(Long id, String name, String createdAt, String updatedAt, List<CategoryDomainDTO> subcategories) {
  public CategoriesDTO(Category category, List<Category> subcategories) {
    this(
      category.getId(),
      category.getName(),
      category.getCreatedAt().toString(),
      category.getUpdatedAt().toString(),
      toCategoryDomainDTOList(subcategories)
    );
  }

  private static List<CategoryDomainDTO> toCategoryDomainDTOList(List<Category> categories) {
    return categories.stream().map(CategoryDomainDTO::new).toList();
  }
}
