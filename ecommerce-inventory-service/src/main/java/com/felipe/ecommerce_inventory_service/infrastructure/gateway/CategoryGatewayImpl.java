package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.category.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.CategoryEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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

  @Override
  public Category updateCategory(Category category, String updatedName) {
    CategoryEntity categoryEntity = this.entityMapper.toEntity(category);
    categoryEntity.setName(updatedName);
    return this.entityMapper.toDomain(this.categoryRepository.save(categoryEntity));
  }

  @Override
  public List<CategoriesDTO> getAllCategories() {
    return this.categoryRepository.findAll()
      .stream()
      .filter(category -> category.getParentCategory() == null)
      .map(category -> {
        Category parentCategory = this.entityMapper.toDomain(category);
        List<Category> subcategories = category.getSubCategories() == null ?
                                       List.of() :
                                       category.getSubCategories().stream().map(this.entityMapper::toDomain).toList();
        return new CategoriesDTO(parentCategory, subcategories);
      })
      .toList();
  }

  @Override
  public Category deleteCategory(Category category) {
    CategoryEntity categoryEntity = this.entityMapper.toEntity(category);
    this.categoryRepository.delete(categoryEntity);
    return category;
  }
}
