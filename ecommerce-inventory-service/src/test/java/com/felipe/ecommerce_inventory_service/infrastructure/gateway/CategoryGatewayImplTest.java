package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.CategoryEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.CategoryRepository;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class CategoryGatewayImplTest {

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private CategoryEntityMapper categoryEntityMapper;

  @InjectMocks
  private CategoryGatewayImpl categoryGateway;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCategorySuccess - Should successfully create and save a CategoryEntity and return the created Category")
  void createCategorySuccess() {
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().getFirst();
    Category categoryDomain = this.dataMock.getCategoriesDomain().getFirst();

    ArgumentCaptor<CategoryEntity> entityCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

    when(this.categoryRepository.save(entityCaptor.capture())).thenReturn(categoryEntity);
    when(this.categoryEntityMapper.toDomain(categoryEntity)).thenReturn(categoryDomain);

    Category createdCategory = this.categoryGateway.createCategory("Hardware");

    // Captor assertion
    assertThat(entityCaptor.getValue().getName()).isEqualTo("Hardware");
    // Created category assertion
    assertThat(createdCategory.getId()).isEqualTo(categoryDomain.getId());
    assertThat(createdCategory.getName()).isEqualTo(categoryDomain.getName());
    assertThat(createdCategory.getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(createdCategory.getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(createdCategory.getParentCategory()).isEqualTo(categoryDomain.getParentCategory());

    verify(this.categoryRepository, times(1)).save(any(CategoryEntity.class));
    verify(this.categoryEntityMapper, times(1)).toDomain(categoryEntity);
  }

  @Test
  @DisplayName("findCategoryByNameReturnsOptionalOfCategory - Should return an optional of Category")
  void findCategoryByNameReturnsOptionalOfCategory() {
    Category categoryDomain = this.dataMock.getCategoriesDomain().getFirst();
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().getFirst();
    final String categoryName = "Hardware";

    when(this.categoryRepository.findByName(categoryName))
      .thenReturn(Optional.of(categoryEntity));
    when(this.categoryEntityMapper.toDomain(categoryEntity)).thenReturn(categoryDomain);

    Optional<Category> foundCategory = this.categoryGateway.findCategoryByName(categoryName);

    assertThat(foundCategory).isPresent();
    assertThat(foundCategory.get().getId()).isEqualTo(categoryDomain.getId());
    assertThat(foundCategory.get().getName()).isEqualTo(categoryDomain.getName());
    assertThat(foundCategory.get().getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(foundCategory.get().getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(foundCategory.get().getParentCategory()).isEqualTo(categoryDomain.getParentCategory());

    verify(this.categoryRepository, times(1)).findByName(categoryName);
    verify(this.categoryEntityMapper, times(1)).toDomain(categoryEntity);
  }

  @Test
  @DisplayName("findCategoryByNameReturnsOptionalEmpty - Should return an optional empty")
  void findCategoryByNameReturnsOptionalEmpty() {
    final String categoryName = "Hardware";

    when(this.categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());

    Optional<Category> foundCategory = this.categoryGateway.findCategoryByName(categoryName);

    assertThat(foundCategory).isEmpty();
    verify(this.categoryRepository, times(1)).findByName(categoryName);
    verify(this.categoryEntityMapper, never()).toDomain(any(CategoryEntity.class));
  }

  @Test
  @DisplayName("findCategoryByIdReturnsOptionalOfCategory - Should return an optional of category")
  void findCategoryByIdReturnsOptionalOfCategory() {
    Category categoryDomain = this.dataMock.getCategoriesDomain().getFirst();
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().getFirst();

    when(this.categoryRepository.findById(categoryEntity.getId())).thenReturn(Optional.of(categoryEntity));
    when(this.categoryEntityMapper.toDomain(categoryEntity)).thenReturn(categoryDomain);

    Optional<Category> foundCategory = this.categoryGateway.findCategoryById(categoryEntity.getId());

    assertThat(foundCategory).isPresent();
    assertThat(foundCategory.get().getId()).isEqualTo(categoryDomain.getId());
    assertThat(foundCategory.get().getName()).isEqualTo(categoryDomain.getName());
    assertThat(foundCategory.get().getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(foundCategory.get().getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(foundCategory.get().getParentCategory()).isEqualTo(categoryDomain.getParentCategory());

    verify(this.categoryRepository, times(1)).findById(categoryEntity.getId());
    verify(this.categoryEntityMapper, times(1)).toDomain(categoryEntity);
  }

  @Test
  @DisplayName("findCategoryByIdReturnsOptionalEmpty - Should return an optional empty if category is not found")
  void findCategoryByIdReturnsOptionalEmpty() {
    final Long categoryId = 1L;

    when(this.categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

    Optional<Category> foundCategory = this.categoryGateway.findCategoryById(categoryId);

    assertThat(foundCategory).isEmpty();
    verify(this.categoryRepository, times(1)).findById(categoryId);
    verify(this.categoryEntityMapper, never()).toDomain(any(CategoryEntity.class));
  }

  @Test
  @DisplayName("createSubcategorySuccess - Should successfully create a subcategory and return it")
  void createSubcategorySuccess() {
    Category parentCategory = this.dataMock.getCategoriesDomain().getFirst();
    Category subcategoryDomain = this.dataMock.getCategoriesDomain().get(1);
    CategoryEntity parentCategoryEntity = this.dataMock.getCategoriesEntity().getFirst();
    CategoryEntity subcategoryEntity = this.dataMock.getCategoriesEntity().get(1);

    ArgumentCaptor<CategoryEntity> entityCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

    when(this.categoryEntityMapper.toEntity(parentCategory)).thenReturn(parentCategoryEntity);
    when(this.categoryRepository.save(entityCaptor.capture())).thenReturn(subcategoryEntity);
    when(this.categoryEntityMapper.toDomain(subcategoryEntity)).thenReturn(subcategoryDomain);

    Category createdSubcategory = this.categoryGateway.createSubcategory(subcategoryEntity.getName(), parentCategory);

    CategoryEntity capturedEntity = entityCaptor.getValue().getParentCategory();
    // Argument captor assertions
    assertThat(capturedEntity.getId()).isEqualTo(parentCategory.getId());
    assertThat(capturedEntity.getName()).isEqualTo(parentCategory.getName());
    assertThat(capturedEntity.getCreatedAt()).isEqualTo(parentCategory.getCreatedAt());
    assertThat(capturedEntity.getUpdatedAt()).isEqualTo(parentCategory.getUpdatedAt());
    assertThat(capturedEntity.getParentCategory()).isNull();
    // Created category assertions
    assertThat(createdSubcategory.getId()).isEqualTo(subcategoryDomain.getId());
    assertThat(createdSubcategory.getName()).isEqualTo(subcategoryDomain.getName());
    assertThat(createdSubcategory.getCreatedAt()).isEqualTo(subcategoryDomain.getCreatedAt());
    assertThat(createdSubcategory.getUpdatedAt()).isEqualTo(subcategoryDomain.getUpdatedAt());
    assertThat(createdSubcategory.getParentCategory()).isNotNull();
    assertThat(createdSubcategory.getParentCategory().getId()).isEqualTo(subcategoryDomain.getParentCategory().getId());
    assertThat(createdSubcategory.getParentCategory().getName()).isEqualTo(subcategoryDomain.getParentCategory().getName());
    assertThat(createdSubcategory.getParentCategory().getCreatedAt()).isEqualTo(subcategoryDomain.getParentCategory().getCreatedAt());
    assertThat(createdSubcategory.getParentCategory().getUpdatedAt()).isEqualTo(subcategoryDomain.getParentCategory().getUpdatedAt());

    verify(this.categoryEntityMapper, times(1)).toEntity(parentCategory);
    verify(this.categoryRepository, times(1)).save(any(CategoryEntity.class));
    verify(this.categoryEntityMapper, times(1)).toDomain(subcategoryEntity);
  }
}
