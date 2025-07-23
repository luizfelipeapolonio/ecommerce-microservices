package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.UpdateCategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private UpdateCategoryUseCaseImpl updateCategoryUseCase;
  private List<Category> categoriesDomain;

  @BeforeEach
  void setUp() {
    this.updateCategoryUseCase = new UpdateCategoryUseCaseImpl(this.categoryGateway);
    this.categoriesDomain = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("updateCategorySuccess - Should successfully update a category and return it")
  void updateCategorySuccess() {
    Category category = this.categoriesDomain.getFirst();
    final String updatedCategoryName = "peripherals";
    Category updatedCategoryDomain = Category.mutate(category).name(updatedCategoryName).build();

    when(this.categoryGateway.findCategoryById(category.getId())).thenReturn(Optional.of(category));
    when(this.categoryGateway.findCategoryByName(updatedCategoryName)).thenReturn(Optional.empty());
    when(this.categoryGateway.updateCategory(category, updatedCategoryName)).thenReturn(updatedCategoryDomain);

    Category updatedCategory = this.updateCategoryUseCase.execute(category.getId(), updatedCategoryName);

    assertThat(updatedCategory.getId()).isEqualTo(category.getId());
    assertThat(updatedCategory.getName()).isEqualTo(updatedCategoryName);
    assertThat(updatedCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
    assertThat(updatedCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
    assertThat(updatedCategory.getParentCategory()).isEqualTo(category.getParentCategory());

    verify(this.categoryGateway, times(1)).findCategoryById(category.getId());
    verify(this.categoryGateway, times(1)).findCategoryByName(updatedCategoryName);
    verify(this.categoryGateway, times(1)).updateCategory(category, updatedCategoryName);
  }

  @Test
  @DisplayName("updateCategoryFailsByCategoryNotFound - Should throw a DataNotFoundException if category is not found")
  void updateCategoryFailsByCategoryNotFound() {
    final Long categoryId = 1L;

    when(this.categoryGateway.findCategoryById(categoryId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateCategoryUseCase.execute(categoryId, "anything"));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Categoria de id '%s' não encontrada", categoryId);

    verify(this.categoryGateway, times(1)).findCategoryById(categoryId);
    verify(this.categoryGateway, never()).findCategoryByName(anyString());
    verify(this.categoryGateway, never()).updateCategory(any(Category.class), anyString());
  }

  @Test
  @DisplayName("updateCategoryFailsByCategoryAlreadyExists - Should throw a CategoryAlreadyExistsException if the new category name already exists")
  void updateCategoryFailsByCategoryAlreadyExists() {
    Category category = this.categoriesDomain.getFirst();
    final String categoryName = "hardware";

    when(this.categoryGateway.findCategoryById(category.getId())).thenReturn(Optional.of(category));
    when(this.categoryGateway.findCategoryByName(categoryName)).thenReturn(Optional.of(category));

    Exception thrown = catchException(() -> this.updateCategoryUseCase.execute(category.getId(), categoryName));

    assertThat(thrown)
      .isExactlyInstanceOf(CategoryAlreadyExistsException.class)
      .hasMessage("A categoria '%s' já existe", categoryName);

    verify(this.categoryGateway, times(1)).findCategoryById(category.getId());
    verify(this.categoryGateway, times(1)).findCategoryByName(categoryName);
    verify(this.categoryGateway, never()).updateCategory(any(Category.class), anyString());
  }
}
