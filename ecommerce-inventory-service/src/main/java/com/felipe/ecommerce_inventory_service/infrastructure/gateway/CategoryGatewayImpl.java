package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.CategoryEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CategoryGatewayImpl implements CategoryGateway {
  private final CategoryRepository categoryRepository;
  private final CategoryEntityMapper entityMapper;

  public CategoryGatewayImpl(CategoryRepository categoryRepository, CategoryEntityMapper entityMapper) {
    this.categoryRepository = categoryRepository;
    this.entityMapper = entityMapper;
  }

  @Override
  public Category createCategory(String name) {
    return this.entityMapper.toDomain(this.categoryRepository.save(new CategoryEntity(name)));
  }

  @Override
  public Optional<Category> findCategoryByName(String name) {
    return this.categoryRepository.findByName(name).map(this.entityMapper::toDomain);
  }

  @Override
  public Optional<Category> findCategoryById(Long id) {
    return this.categoryRepository.findById(id).map(this.entityMapper::toDomain);
  }

  @Override
  public Category createSubcategory(String subcategoryName, Category parentCategory) {
    CategoryEntity subcategory = new CategoryEntity(subcategoryName);
    subcategory.setParentCategory(this.entityMapper.toEntity(parentCategory));
    return this.entityMapper.toDomain(this.categoryRepository.save(subcategory));
  }
}
