package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl.GetAllSubcategoriesUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetAllSubcategoriesUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private GetAllSubcategoriesUseCaseImpl getAllSubcategoriesUseCase;
  private List<Category> categoriesDomain;

  @BeforeEach
  void setUp() {
    this.getAllSubcategoriesUseCase = new GetAllSubcategoriesUseCaseImpl(categoryGateway);
    this.categoriesDomain = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("getAllSubcategoriesSuccess - Should successfully get all the found subcategories")
  void getAllSubcategoriesSuccess() {
    List<Category> subcategories = this.categoriesDomain.stream()
      .filter(category -> category.getParentCategory() != null)
      .toList();

    when(this.categoryGateway.getAllSubcategories()).thenReturn(subcategories);

    List<Category> foundSubcategories = this.getAllSubcategoriesUseCase.execute();

    assertThat(foundSubcategories.size()).isEqualTo(subcategories.size());
    verify(this.categoryGateway, times(1)).getAllSubcategories();
  }
}
