package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.CreateCategoryUseCaseImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private CreateCategoryUseCaseImpl createCategoryUseCase;
  private List<Category> categoriesMock;

  @BeforeEach
  void setUp() {
    this.createCategoryUseCase = new CreateCategoryUseCaseImpl(this.categoryGateway);
    this.categoriesMock = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("createCategorySuccess - Should successfully create a category and return it")
  void createCategorySuccess() {
    Category category = this.categoriesMock.getFirst();

    when(this.categoryGateway.findCategoryByName(category.getName())).thenReturn(Optional.empty());
    when(this.categoryGateway.createCategory(category.getName())).thenReturn(category);

    Category createdCategory = this.createCategoryUseCase.execute(category.getName());

    assertThat(createdCategory.getId()).isEqualTo(category.getId());
    assertThat(createdCategory.getName()).isEqualTo(category.getName());
    assertThat(createdCategory.getCreatedAt()).isEqualTo(category.getCreatedAt());
    assertThat(createdCategory.getUpdatedAt()).isEqualTo(category.getUpdatedAt());
    assertThat(createdCategory.getParentCategory()).isNull();

    verify(this.categoryGateway, times(1)).findCategoryByName(category.getName());
    verify(this.categoryGateway, times(1)).createCategory(category.getName());
  }

  @Test
  @DisplayName("createCategoryFailsByCategoryAlreadyExistsException - Should throw a CategoryAlreadyExistsException if a category already exists")
  void createCategoryFailsByCategoryAlreadyExistsException() {
    Category category = this.categoriesMock.getFirst();

    when(this.categoryGateway.findCategoryByName(category.getName())).thenReturn(Optional.of(category));

    Exception thrown = catchException(() -> this.createCategoryUseCase.execute(category.getName()));

    assertThat(thrown)
      .isExactlyInstanceOf(CategoryAlreadyExistsException.class)
      .hasMessage("A categoria '%s' já existe", category.getName());

    verify(this.categoryGateway, times(1)).findCategoryByName(category.getName());
    verify(this.categoryGateway, never()).createCategory(anyString());
  }
}
