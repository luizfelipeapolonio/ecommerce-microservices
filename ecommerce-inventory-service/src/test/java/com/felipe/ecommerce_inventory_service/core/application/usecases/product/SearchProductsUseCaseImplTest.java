package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.SearchProductsResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.SearchProductsUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SearchProductsUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private SearchProductsUseCase searchProductsUseCase;

  @BeforeEach
  void setUp() {
    this.searchProductsUseCase = new SearchProductsUseCaseImpl(this.productGateway);
  }

  @Test
  @DisplayName("searchProductSuccess - Should successfully search for products and return it")
  void searchProductSuccess() {
    SearchProductsResponseDTO searchResponse = new SearchProductsResponseDTO(
      1,
      1,
      10,
      1,
      List.of(new SearchProductsResponseDTO.SearchedProducts(
        UUID.fromString("132a26bc-f508-44e0-8cf8-0ed24c2e4f22"),
        "Product 1",
        "120.00",
        null,
        null,
        "120.00",
        1L,
        "Brand 1",
        "Category 1",
        "Model 1",
        "/path/to/product-image/product_1.jpg"
      ))
    );

    when(this.productGateway.searchProducts("product 1", 1, 60)).thenReturn(searchResponse);

    SearchProductsResponseDTO search = this.searchProductsUseCase.execute("product 1", 1, 60);

    assertThat(search).usingRecursiveComparison().isEqualTo(searchResponse);
    verify(this.productGateway, times(1)).searchProducts("product 1", 1, 60);
  }
}