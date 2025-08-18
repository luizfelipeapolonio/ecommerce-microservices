package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.dtos.category.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.impl.GetAllCategoriesUseCaseImpl;
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
public class GetAllCategoriesUseCaseImplTest {

  @Mock
  private CategoryGateway categoryGateway;

  private GetAllCategoriesUseCaseImpl getAllCategoriesUseCase;
  private List<Category> categoriesDomain;

  @BeforeEach
  void setUp() {
    this.getAllCategoriesUseCase = new GetAllCategoriesUseCaseImpl(this.categoryGateway);
    this.categoriesDomain = new DataMock().getCategoriesDomain();
  }

  @Test
  @DisplayName("getAllCategoriesReturnsFilledList - Should return a list of Category")
  void getAllCategoriesReturnsFilledList() {
    CategoriesDTO category1 = new CategoriesDTO(this.categoriesDomain.get(0), List.of(this.categoriesDomain.get(1), this.categoriesDomain.get(2)));
    CategoriesDTO category2 = new CategoriesDTO(this.categoriesDomain.get(3), List.of(this.categoriesDomain.get(4)));
    List<CategoriesDTO> categoriesDTO = List.of(category1, category2);

    when(this.categoryGateway.getAllCategories()).thenReturn(categoriesDTO);

    List<CategoriesDTO> categories = this.getAllCategoriesUseCase.execute();

    assertThat(categories.size()).isEqualTo(categoriesDTO.size());
    assertThat(categories.stream().map(CategoriesDTO::id).toList())
      .containsExactlyInAnyOrderElementsOf(categoriesDTO.stream().map(CategoriesDTO::id).toList());

    verify(this.categoryGateway, times(1)).getAllCategories();
  }
}
