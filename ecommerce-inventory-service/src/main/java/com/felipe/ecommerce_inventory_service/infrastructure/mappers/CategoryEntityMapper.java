package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoryEntityMapper {
  public Category toDomain(CategoryEntity entity) {
    return Category.builder()
      .id(entity.getId())
      .name(entity.getName())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .parentCategory(entity.getParentCategory() == null ? null : this.toDomain(entity.getParentCategory()))
      .build();
  }

  public CategoryEntity toEntity(Category domain) {
    CategoryEntity entity = new CategoryEntity();
    entity.setId(domain.getId());
    entity.setName(domain.getName());
    entity.setCreatedAt(domain.getCreatedAt());
    entity.setUpdatedAt(domain.getUpdatedAt());
    entity.setParentCategory(domain.getParentCategory() == null ? null : this.toEntity(domain.getParentCategory()));
    return entity;
  }
}
