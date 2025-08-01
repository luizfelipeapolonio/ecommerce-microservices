package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl.GetCategoryByIdUseCaseImpl;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private GetCategoryByIdUseCaseImpl getCategoryByIdUseCase;
  private List<Category> categoriesDomain;

  @BeforeEach
  void setUp() {
    this.getCategoryByIdUseCase = new GetCategoryByIdUseCaseImpl(this.categoryGateway);
    this.categoriesDomain = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("getCategoryByIdSuccess - Should successfully find a category by id and return it")
  void getCategoryByIdSuccess() {
    Category category = this.categoriesDomain.getFirst();

    when(this.categoryGateway.findCategoryById(category.getId())).thenReturn(Optional.of(category));

    Category foundCategory = this.getCategoryByIdUseCase.execute(category.getId());

    assertThat(foundCategory.getId()).isEqualTo(category.getId());
    assertThat(foundCategory.getName()).isEqualTo(category.getName());
    assertThat(foundCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
    assertThat(foundCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
    assertThat(foundCategory.getParentCategory()).isEqualTo(category.getParentCategory());

    verify(this.categoryGateway, times(1)).findCategoryById(category.getId());
  }

  @Test
  @DisplayName("getCategoryByIdFailsByCategoryNotFound - Should throw a DataNotFoundException if the category is not found")
  void getCategoryByIdFailsByCategoryNotFound() {
    final Long categoryId = 1L;

    when(this.categoryGateway.findCategoryById(categoryId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getCategoryByIdUseCase.execute(categoryId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Categoria de id '%s' não encontrada", categoryId);

    verify(this.categoryGateway, times(1)).findCategoryById(categoryId);
  }
}
