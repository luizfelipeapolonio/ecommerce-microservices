package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.CreateSubcategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class CreateSubcategoryUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private CreateSubcategoryUseCaseImpl createSubcategoryUseCase;
  private List<Category> categoriesDomain;

  @BeforeEach
  void setUp() {
    this.createSubcategoryUseCase = new CreateSubcategoryUseCaseImpl(this.categoryGateway);
    this.categoriesDomain = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("createSubcategorySuccess - Should successfully create a subcategory and return it")
  void createSubcategorySuccess() {
    Category parentCategory = categoriesDomain.getFirst();
    Category subcategory = categoriesDomain.get(1);

    when(this.categoryGateway.findCategoryByName(subcategory.getName())).thenReturn(Optional.empty());
    when(this.categoryGateway.findCategoryById(1L)).thenReturn(Optional.of(parentCategory));
    when(this.categoryGateway.createSubcategory(subcategory.getName(), parentCategory)).thenReturn(subcategory);

    Category createdSubcategory = this.createSubcategoryUseCase.execute(parentCategory.getId(), subcategory.getName());

    assertThat(createdSubcategory.getId()).isEqualTo(subcategory.getId());
    assertThat(createdSubcategory.getName()).isEqualTo(subcategory.getName());
    assertThat(createdSubcategory.getCreatedAt()).isEqualTo(subcategory.getCreatedAt());
    assertThat(createdSubcategory.getUpdatedAt()).isEqualTo(subcategory.getUpdatedAt());
    assertThat(createdSubcategory.getParentCategory()).isNotNull();
    assertThat(createdSubcategory.getParentCategory().getId()).isEqualTo(parentCategory.getId());
    assertThat(createdSubcategory.getParentCategory().getName()).isEqualTo(parentCategory.getName());
    assertThat(createdSubcategory.getParentCategory().getCreatedAt()).isEqualTo(parentCategory.getCreatedAt());
    assertThat(createdSubcategory.getParentCategory().getUpdatedAt()).isEqualTo(parentCategory.getUpdatedAt());
    assertThat(createdSubcategory.getParentCategory().getParentCategory()).isNull();

    verify(this.categoryGateway, times(1)).findCategoryByName(subcategory.getName());
    verify(this.categoryGateway, times(1)).findCategoryById(parentCategory.getId());
    verify(this.categoryGateway, times(1)).createSubcategory(subcategory.getName(), parentCategory);
  }

  @Test
  @DisplayName("createSubcategoryFailsByCategoryAlreadyExists - Should throw a CategoryAlreadyExistsException if the given subcategory name already exists")
  void createSubcategoryFailsByCategoryAlreadyExists() {
    Category subcategory = categoriesDomain.get(1);

    when(this.categoryGateway.findCategoryByName(subcategory.getName())).thenReturn(Optional.of(subcategory));

    Exception thrown = catchException(() -> this.createSubcategoryUseCase.execute(1L, subcategory.getName()));

    assertThat(thrown)
      .isExactlyInstanceOf(CategoryAlreadyExistsException.class)
      .hasMessage("A categoria '%s' já existe", subcategory.getName());

    verify(this.categoryGateway, times(1)).findCategoryByName(subcategory.getName());
    verify(this.categoryGateway, never()).findCategoryById(any(Long.class));
    verify(this.categoryGateway, never()).createSubcategory(anyString(), any(Category.class));
  }

  @Test
  @DisplayName("createSubcategoryFailsByParentCategoryNotFound - Should throw a DataNotFoundException if parent category is not found")
  void createSubcategoryFailsByParentCategoryNotFound() {
    final String subcategoryName = "Motherboards";
    final Long parentCategoryId = 1L;

    when(this.categoryGateway.findCategoryByName(subcategoryName)).thenReturn(Optional.empty());
    when(this.categoryGateway.findCategoryById(parentCategoryId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.createSubcategoryUseCase.execute(parentCategoryId, subcategoryName));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Categoria de id '%s' não encontrada", parentCategoryId);

    verify(this.categoryGateway, times(1)).findCategoryByName(subcategoryName);
    verify(this.categoryGateway, times(1)).findCategoryById(parentCategoryId);
    verify(this.categoryGateway, never()).createSubcategory(anyString(), any(Category.class));
  }
}
