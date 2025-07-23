package com.felipe.ecommerce_inventory_service.testutils;

import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataMock {
  private final List<Category> categoriesDomain = new ArrayList<>();
  private final List<CategoryEntity> categoriesEntity = new ArrayList<>();

  public DataMock() {
    this.createCategoriesDomainMock();
    this.createCategoriesEntityMock();
  }

  public List<Category> getCategoriesDomain() {
    return this.categoriesDomain;
  }

  public List<CategoryEntity> getCategoriesEntity() {
    return this.categoriesEntity;
  }

  private void createCategoriesDomainMock() {
    Category category1 = Category.builder()
      .id(1L)
      .name("hardware")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Category category2 = Category.builder()
      .id(2L)
      .name("motherboards")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .parentCategory(category1)
      .build();

    this.categoriesDomain.add(category1);
    this.categoriesDomain.add(category2);
  }

  private void createCategoriesEntityMock() {
    CategoryEntity category1 = new CategoryEntity();
    category1.setId(1L);
    category1.setName("hardware");
    category1.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category1.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));

    CategoryEntity category2 = new CategoryEntity();
    category2.setId(2L);
    category2.setName("motherboards");
    category2.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category2.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category2.setParentCategory(category1);

    this.categoriesEntity.add(category1);
    this.categoriesEntity.add(category2);
  }
}
