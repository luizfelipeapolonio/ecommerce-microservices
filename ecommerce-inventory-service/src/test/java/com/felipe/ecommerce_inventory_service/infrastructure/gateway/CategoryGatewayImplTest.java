package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.CategoriesDTO;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
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

  @Test
  @DisplayName("updateCategorySuccess - Should successfully update a category")
  void updateCategorySuccess() {
    Category category = this.dataMock.getCategoriesDomain().getFirst();
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().getFirst();
    final String updatedCategoryName = "peripherals";
    Category updatedCategoryDomain = Category.mutate(category).name(updatedCategoryName).build();

    ArgumentCaptor<CategoryEntity> entityCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

    when(this.categoryEntityMapper.toEntity(category)).thenReturn(categoryEntity);
    when(this.categoryRepository.save(entityCaptor.capture())).thenReturn(categoryEntity);
    when(this.categoryEntityMapper.toDomain(categoryEntity)).thenReturn(updatedCategoryDomain);

    Category updatedCategory = this.categoryGateway.updateCategory(category, updatedCategoryName);

    // Argument captor assertion
    assertThat(entityCaptor.getValue().getName()).isEqualTo(updatedCategoryName);
    // updated category assertions
    assertThat(updatedCategory.getId()).isEqualTo(updatedCategoryDomain.getId());
    assertThat(updatedCategory.getName()).isEqualTo(updatedCategoryDomain.getName());
    assertThat(updatedCategory.getCreatedAt()).isEqualTo(updatedCategoryDomain.getCreatedAt());
    assertThat(updatedCategory.getUpdatedAt()).isEqualTo(updatedCategoryDomain.getUpdatedAt());
    assertThat(updatedCategory.getParentCategory()).isEqualTo(updatedCategoryDomain.getParentCategory());

    verify(this.categoryEntityMapper, times(1)).toEntity(category);
    verify(this.categoryRepository, times(1)).save(categoryEntity);
    verify(this.categoryEntityMapper, times(1)).toDomain(categoryEntity);
  }

  @Test
  @DisplayName("getAllCategoriesSuccess - Should successfully return a list of Category")
  void getAllCategoriesSuccess() {
    CategoryEntity category1 = this.dataMock.getCategoriesEntity().get(0);
    CategoryEntity category2 = this.dataMock.getCategoriesEntity().get(1);
    CategoryEntity category3 = this.dataMock.getCategoriesEntity().get(2);
    CategoryEntity category4 = this.dataMock.getCategoriesEntity().get(3);
    CategoryEntity category5 = this.dataMock.getCategoriesEntity().get(4);
    List<Category> categoriesDomain = this.dataMock.getCategoriesDomain();

    // Setting subcategories property
    category1.setSubCategories(List.of(category2, category3));
    category4.setSubCategories(List.of(category5));

    List<CategoryEntity> categoriesEntity = List.of(category1, category2, category3, category4, category5);

    when(this.categoryRepository.findAll()).thenReturn(categoriesEntity);
    when(this.categoryEntityMapper.toDomain(categoriesEntity.get(0))).thenReturn(categoriesDomain.get(0));
    when(this.categoryEntityMapper.toDomain(categoriesEntity.get(1))).thenReturn(categoriesDomain.get(1));
    when(this.categoryEntityMapper.toDomain(categoriesEntity.get(2))).thenReturn(categoriesDomain.get(2));
    when(this.categoryEntityMapper.toDomain(categoriesEntity.get(3))).thenReturn(categoriesDomain.get(3));
    when(this.categoryEntityMapper.toDomain(categoriesEntity.get(4))).thenReturn(categoriesDomain.get(4));

    List<CategoriesDTO> categories = this.categoryGateway.getAllCategories();

    assertThat(categories.size()).isEqualTo(2);
    assertThat(categories.get(0).subcategories().size()).isEqualTo(2);
    assertThat(categories.get(1).subcategories().size()).isEqualTo(1);

    verify(this.categoryRepository, times(1)).findAll();
    verify(this.categoryEntityMapper, times(5)).toDomain(any(CategoryEntity.class));
    verify(this.categoryEntityMapper, times(1)).toDomain(categoriesEntity.get(0));
    verify(this.categoryEntityMapper, times(1)).toDomain(categoriesEntity.get(1));
    verify(this.categoryEntityMapper, times(1)).toDomain(categoriesEntity.get(2));
    verify(this.categoryEntityMapper, times(1)).toDomain(categoriesEntity.get(3));
    verify(this.categoryEntityMapper, times(1)).toDomain(categoriesEntity.get(4));
  }

  @Test
  @DisplayName("deleteCategorySuccess - Should successfully delete a category and return the deleted category")
  void deleteCategorySuccess() {
    Category categoryDomain = this.dataMock.getCategoriesDomain().getFirst();
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().getFirst();
    ArgumentCaptor<CategoryEntity> entityCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

    when(this.categoryEntityMapper.toEntity(categoryDomain)).thenReturn(categoryEntity);
    doNothing().when(this.categoryRepository).delete(entityCaptor.capture());

    Category deletedCategory = this.categoryGateway.deleteCategory(categoryDomain);

    // argument captor assertions
    assertThat(entityCaptor.getValue().getId()).isEqualTo(categoryEntity.getId());
    assertThat(entityCaptor.getValue().getName()).isEqualTo(categoryEntity.getName());
    assertThat(entityCaptor.getValue().getCreatedAt()).isEqualTo(categoryEntity.getCreatedAt());
    assertThat(entityCaptor.getValue().getUpdatedAt()).isEqualTo(categoryEntity.getUpdatedAt());
    assertThat(entityCaptor.getValue().getParentCategory()).isEqualTo(categoryEntity.getParentCategory());
    // deleted category assertions
    assertThat(deletedCategory.getId()).isEqualTo(categoryDomain.getId());
    assertThat(deletedCategory.getName()).isEqualTo(categoryDomain.getName());
    assertThat(deletedCategory.getCreatedAt()).isEqualTo(categoryDomain.getCreatedAt());
    assertThat(deletedCategory.getUpdatedAt()).isEqualTo(categoryDomain.getUpdatedAt());
    assertThat(deletedCategory.getParentCategory()).isEqualTo(categoryDomain.getParentCategory());

    verify(this.categoryEntityMapper, times(1)).toEntity(categoryDomain);
    verify(this.categoryRepository, times(1)).delete(categoryEntity);
  }
}
