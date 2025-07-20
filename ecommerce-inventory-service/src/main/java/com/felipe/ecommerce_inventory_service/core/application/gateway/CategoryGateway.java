package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.Optional;

public interface CategoryGateway {
  Category createCategory(String name);
  Optional<Category> findCategoryByName(String name);
  Optional<Category> findCategoryById(Long id);
  Category createSubcategory(String subcategoryName, Category parentCategory);
}
