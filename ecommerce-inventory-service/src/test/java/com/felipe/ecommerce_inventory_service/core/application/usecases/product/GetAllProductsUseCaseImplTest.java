package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetAllProductsUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
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
public class GetAllProductsUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private GetAllProductsUseCaseImpl getAllProductsUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.getAllProductsUseCase = new GetAllProductsUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("getAllProductsSuccess - Should successfully get all products")
  void getAllProductsSuccess() {
    final List<ProductDTO> productDTOs = List.of(
      new ProductDTO(this.productsDomain.get(0), List.of()),
      new ProductDTO(this.productsDomain.get(1), List.of()),
      new ProductDTO(this.productsDomain.get(2), List.of())
    );
    final PageResponseDTO productResponse = new ProductPageResponseDTO(0, 1, 1, 1, productDTOs);

    when(this.productGateway.getAllProducts(0, 10)).thenReturn(productResponse);

    PageResponseDTO productsPage = this.getAllProductsUseCase.execute(0, 10);

    assertThat(productsPage.currentPage()).isEqualTo(productResponse.currentPage());
    assertThat(productsPage.currentElements()).isEqualTo(productResponse.currentElements());
    assertThat(productsPage.totalPages()).isEqualTo(productResponse.totalPages());
    assertThat(productsPage.totalElements()).isEqualTo(productResponse.totalElements());
    assertThat(productsPage.content().size()).isEqualTo(productResponse.content().size());
    assertThat(productsPage.content().get(0)).usingRecursiveComparison().isEqualTo(productResponse.content().get(0));
    assertThat(productsPage.content().get(1)).usingRecursiveComparison().isEqualTo(productResponse.content().get(1));
    assertThat(productsPage.content().get(2)).usingRecursiveComparison().isEqualTo(productResponse.content().get(2));

    verify(this.productGateway, times(1)).getAllProducts(0, 10);
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
                            boolean withDiscount,
                            String discountType,
                            String discountValue,
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
        product.isItWithDiscount(),
        product.getDiscountType() == null ? null : product.getDiscountType(),
        product.getDiscountValue() == null ? null : product.getDiscountValue(),
        product.getCreatedAt().toString(),
        product.getUpdatedAt().toString(),
        images
      );
    }
  }
}
