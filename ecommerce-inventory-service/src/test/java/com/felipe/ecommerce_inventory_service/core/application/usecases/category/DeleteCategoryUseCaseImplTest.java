package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.DeleteCategoryUseCaseImpl;
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

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private DeleteCategoryUseCaseImpl deleteCategoryUseCase;
  private List<Category> categoriesDomain;

  @BeforeEach
  void setUp() {
    this.deleteCategoryUseCase = new DeleteCategoryUseCaseImpl(this.categoryGateway);
    this.categoriesDomain = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("deleteCategoryUseCaseSuccess - Should successfully delete a category and return it")
  void deleteCategoryUseCaseSuccess() {
    Category category = this.categoriesDomain.getFirst();

    when(this.categoryGateway.findCategoryById(category.getId())).thenReturn(Optional.of(category));
    when(this.categoryGateway.deleteCategory(category)).thenReturn(category);

    Category deletedCategory = this.deleteCategoryUseCase.execute(category.getId());

    assertThat(deletedCategory.getId()).isEqualTo(category.getId());
    assertThat(deletedCategory.getName()).isEqualTo(category.getName());
    assertThat(deletedCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
    assertThat(deletedCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
    assertThat(deletedCategory.getParentCategory()).isEqualTo(category.getParentCategory());

    verify(this.categoryGateway, times(1)).findCategoryById(category.getId());
    verify(this.categoryGateway, times(1)).deleteCategory(category);
  }

  @Test
  @DisplayName("deleteCategoryFailsByCategoryNotFound - Should throw a DataNotFoundException if category is not found")
  void deleteCategoryFailsByCategoryNotFound() {
    final Long categoryId = 1L;

    when(this.categoryGateway.findCategoryById(categoryId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deleteCategoryUseCase.execute(categoryId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Categoria de id '%s' não encontrada", categoryId);

    verify(this.categoryGateway, times(1)).findCategoryById(categoryId);
    verify(this.categoryGateway, never()).deleteCategory(any(Category.class));
  }
}
