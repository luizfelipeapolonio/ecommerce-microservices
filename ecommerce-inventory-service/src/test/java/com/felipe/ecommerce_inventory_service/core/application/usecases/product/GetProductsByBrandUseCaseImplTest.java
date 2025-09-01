package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsByBrandUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
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
public class GetProductsByBrandUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  @Mock
  private BrandGateway brandGateway;

  private GetProductsByBrandUseCaseImpl getProductsByBrandUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getProductsByBrandUseCase = new GetProductsByBrandUseCaseImpl(this.productGateway, this.brandGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getProductsByBrandSuccess - Should successfully get products by brand and return a page of products")
  void getProductsByBrandSuccess() {
    final Brand brand = this.dataMock.getBrandsDomain().getFirst();
    final List<ProductDTO> productDTOs = Stream.of(this.dataMock.getProductsDomain().get(0), this.dataMock.getProductsDomain().get(1))
      .map(product -> new ProductDTO(product, List.of()))
      .toList();
    final PageResponseDTO productResponse = new ProductPageResponseDTO(0, 2, 1, 2, productDTOs);

    when(this.brandGateway.findBrandByName(brand.getName())).thenReturn(Optional.of(brand));
    when(this.productGateway.getProductsByBrand(brand.getName(), 0, 10)).thenReturn(productResponse);

    PageResponseDTO productsPage = this.getProductsByBrandUseCase.execute(brand.getName(), 0, 10);

    assertThat(productsPage.currentPage()).isEqualTo(productResponse.currentPage());
    assertThat(productsPage.currentElements()).isEqualTo(productResponse.currentElements());
    assertThat(productsPage.totalPages()).isEqualTo(productResponse.totalPages());
    assertThat(productsPage.totalElements()).isEqualTo(productResponse.totalElements());
    assertThat(productsPage.content().size()).isEqualTo(productResponse.content().size());
    assertThat(productsPage.content().get(0)).usingRecursiveComparison().isEqualTo(productResponse.content().get(0));
    assertThat(productsPage.content().get(1)).usingRecursiveComparison().isEqualTo(productResponse.content().get(1));

    verify(this.brandGateway, times(1)).findBrandByName(brand.getName());
    verify(this.productGateway, times(1)).getProductsByBrand(brand.getName(), 0, 10);
  }

  @Test
  @DisplayName("getProductsByBrandFailsByBrandNotFound - Should throw a DataNotFoundException if the given brand is not found")
  void getProductsByBrandFailsByBrandNotFound() {
    final String brandName = "logitech";

    when(this.brandGateway.findBrandByName(brandName)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getProductsByBrandUseCase.execute(brandName, 0, 10));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Marca '%s' não encontrada", brandName);

    verify(this.brandGateway, times(1)).findBrandByName(brandName);
    verify(this.productGateway, never()).getProductsByBrand(anyString(), anyInt(), anyInt());
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
