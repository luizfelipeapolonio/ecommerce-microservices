package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsByCategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;

@ExtendWith(MockitoExtension.class)
public class GetProductsByCategoryUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  @Mock
  private CategoryGateway categoryGateway;

  private GetProductsByCategoryUseCaseImpl getProductsByCategoryUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getProductsByCategoryUseCase = new GetProductsByCategoryUseCaseImpl(this.productGateway, this.categoryGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getProductsByCategorySuccess - Should successfully get products by category and return a page of products")
  void getProductsByCategorySuccess() {
    final Category category = this.dataMock.getCategoriesDomain().getFirst();
    final List<ProductDTO> productDTOs = Stream.of(this.dataMock.getProductsDomain().get(0), this.dataMock.getProductsDomain().get(1))
      .map(product -> new ProductDTO(product, List.of()))
      .toList();
    final PageResponseDTO productResponse = new ProductPageResponseDTO(0, 2, 5, 10, productDTOs);

    when(this.categoryGateway.findCategoryByName(category.getName())).thenReturn(Optional.of(category));
    when(this.productGateway.getProductsByCategory(category.getName(), 0, 10)).thenReturn(productResponse);

    PageResponseDTO productsPage = this.getProductsByCategoryUseCase.execute(category.getName(), 0, 10);

    assertThat(productsPage.currentPage()).isEqualTo(productResponse.currentPage());
    assertThat(productsPage.currentElements()).isEqualTo(productResponse.currentElements());
    assertThat(productsPage.totalPages()).isEqualTo(productResponse.totalPages());
    assertThat(productsPage.totalElements()).isEqualTo(productResponse.totalElements());
    assertThat(productsPage.content().size()).isEqualTo(productResponse.content().size());
    assertThat(productsPage.content().get(0)).usingRecursiveComparison().isEqualTo(productResponse.content().get(0));
    assertThat(productsPage.content().get(1)).usingRecursiveComparison().isEqualTo(productResponse.content().get(1));

    verify(this.categoryGateway, times(1)).findCategoryByName(category.getName());
    verify(this.productGateway, times(1)).getProductsByCategory(category.getName(), 0, 10);
  }

  @Test
  @DisplayName("getProductsByCategoryFailsByCategoryNotFound - Should throw a DataNotFoundException if the given category is not found")
  void getProductsByCategoryFailsByCategoryNotFound() {
    final String categoryName = "mouse";

    when(this.categoryGateway.findCategoryByName(categoryName)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getProductsByCategoryUseCase.execute(categoryName, 0, 10));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Categoria '%s' não encontrada", categoryName);

    verify(this.categoryGateway, times(1)).findCategoryByName(categoryName);
    verify(this.productGateway, never()).getProductsByCategory(anyString(), anyInt(), anyInt());
  }

  private record ProductPageResponseDTO(int currentPage,
                                        int currentElements,
                                        int totalPages,
                                        long totalElements,
                                        List<ProductDTO> content) implements PageResponseDTO {
  }

  private record ProductDTO(String id,
                            String name,
                            String description,
                            String unitPrice,
                            long quantity,
                            String createdAt,
                            String updatedAt,
                            List<ImageFileDTO> images) implements ProductResponseDTO {
    public ProductDTO(Product product, List<ImageFileDTO> images) {
      this(
        product.getId().toString(),
        product.getName(),
        product.getDescription(),
        product.getUnitPrice().toString(),
        product.getQuantity(),
        product.getCreatedAt().toString(),
        product.getUpdatedAt().toString(),
        images
      );
    }
  }
}
