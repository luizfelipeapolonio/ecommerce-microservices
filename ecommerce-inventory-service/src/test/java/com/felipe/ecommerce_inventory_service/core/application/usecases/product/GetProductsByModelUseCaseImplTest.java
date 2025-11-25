package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsByModelUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
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
public class GetProductsByModelUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  @Mock
  private ModelGateway modelGateway;

  private GetProductsByModelUseCaseImpl getProductsByModelUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getProductsByModelUseCase = new GetProductsByModelUseCaseImpl(this.productGateway, this.modelGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getProductsByModelSuccess - Should successfully get products with the given model and brand name")
  void getProductsByModelSuccess() {
    final String modelName = "g pro";
    final String brandName = "logitech";
    final Model existingModel = this.dataMock.getModelsDomain().getFirst();
    final List<ProductDTO> productDTOs = Stream.of(this.dataMock.getProductsDomain().get(0))
      .map(product -> new ProductDTO(product, List.of()))
      .toList();
    final PageResponseDTO productResponse = new ProductPageResponseDTO(0, 1, 1, 1, productDTOs);

    when(this.modelGateway.findModelByNameAndBrandName(modelName, brandName)).thenReturn(Optional.of(existingModel));
    when(this.productGateway.getProductsByModel(modelName, brandName, 0, 10)).thenReturn(productResponse);

    PageResponseDTO productsPage = this.getProductsByModelUseCase.execute(modelName, brandName, 0, 10);

    assertThat(productsPage.currentPage()).isEqualTo(productResponse.currentPage());
    assertThat(productsPage.currentElements()).isEqualTo(productResponse.currentElements());
    assertThat(productsPage.totalPages()).isEqualTo(productResponse.totalPages());
    assertThat(productsPage.totalElements()).isEqualTo(productResponse.totalElements());
    assertThat(productsPage.content().size()).isEqualTo(productResponse.content().size());
    assertThat(productsPage.content().get(0)).usingRecursiveComparison().isEqualTo(productResponse.content().get(0));

    verify(this.modelGateway, times(1)).findModelByNameAndBrandName(modelName, brandName);
    verify(this.productGateway, times(1)).getProductsByModel(modelName, brandName, 0, 10);
  }

  @Test
  @DisplayName("getProductsByModelFailsByModelNotFound - Should throw a DataNotFoundException if the model with the given name and brand name is not found")
  void getProductsByModelFailsByModelNotFound() {
    final String modelName = "g pro";
    final String brandName = "logitech";

    when(this.modelGateway.findModelByNameAndBrandName(modelName, brandName)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getProductsByModelUseCase.execute(modelName, brandName, 0, 10));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Modelo '%s' da marca '%s' não encontrado", modelName, brandName);

    verify(this.modelGateway, times(1)).findModelByNameAndBrandName(modelName, brandName);
    verify(this.productGateway, never()).getProductsByModel(anyString(), anyString(), anyInt(), anyInt());
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
