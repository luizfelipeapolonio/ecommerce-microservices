package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ProductAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UpdateProductUseCaseImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class UpdateProductUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private UpdateProductUseCaseImpl updateProductUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.updateProductUseCase = new UpdateProductUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("updateProductSuccess - Should successfully update a product and return it")
  void updateProductSuccess() {
    Product product = this.productsDomain.getFirst();
    UpdateProductDomainDTO productDTO = new UpdateProductDTO(product);

    when(this.productGateway.findProductById(product.getId())).thenReturn(Optional.of(product));
    when(this.productGateway.findProductByName(productDTO.name())).thenReturn(Optional.empty());
    when(this.productGateway.updateProduct(product, productDTO)).thenReturn(product);

    Product updatedProduct = this.updateProductUseCase.execute(product.getId(), productDTO);

    assertThat(updatedProduct.getId()).isEqualTo(product.getId());
    assertThat(updatedProduct.getName()).isEqualTo(product.getName());
    assertThat(updatedProduct.getDescription()).isEqualTo(product.getDescription());
    assertThat(updatedProduct.getUnitPrice().toString()).isEqualTo(product.getUnitPrice().toString());
    assertThat(updatedProduct.getQuantity()).isEqualTo(product.getQuantity());
    assertThat(updatedProduct.getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(updatedProduct.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(updatedProduct.getCategory()).usingRecursiveComparison().isEqualTo(product.getCategory());
    assertThat(updatedProduct.getBrand()).usingRecursiveComparison().isEqualTo(product.getBrand());
    assertThat(updatedProduct.getModel()).usingRecursiveComparison().isEqualTo(product.getModel());

    verify(this.productGateway, times(1)).findProductById(product.getId());
    verify(this.productGateway, times(1)).findProductByName(productDTO.name());
    verify(this.productGateway, times(1)).updateProduct(product, productDTO);
  }

  @Test
  @DisplayName("updateProductFailsByProductNotFound - Should throw a DataNotFoundException if the product with the given id is not found")
  void updateProductFailsByProductNotFound() {
    Product product = this.productsDomain.getFirst();
    UpdateProductDomainDTO productDTO = new UpdateProductDTO(product);

    when(this.productGateway.findProductById(product.getId())).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateProductUseCase.execute(product.getId(), productDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id '%s' não encontrado", product.getId());

    verify(this.productGateway, times(1)).findProductById(product.getId());
    verify(this.productGateway, never()).findProductByName(anyString());
    verify(this.productGateway, never()).updateProduct(any(Product.class), any(UpdateProductDomainDTO.class));
  }

  @Test
  @DisplayName("updateProductFailsByProductAlreadyExists - Should throw a ProductAlreadyExistsException if the product with the given name already exists")
  void updateProductFailsByProductAlreadyExists() {
    Product product = this.productsDomain.getFirst();
    UpdateProductDomainDTO productDTO = new UpdateProductDTO(product);

    when(this.productGateway.findProductById(product.getId())).thenReturn(Optional.of(product));
    when(this.productGateway.findProductByName(productDTO.name())).thenReturn(Optional.of(product));

    Exception thrown = catchException(() -> this.updateProductUseCase.execute(product.getId(), productDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(ProductAlreadyExistsException.class)
      .hasMessage("O produto '%s' já existe", productDTO.name());

    verify(this.productGateway, times(1)).findProductById(product.getId());
    verify(this.productGateway, times(1)).findProductByName(productDTO.name());
    verify(this.productGateway, never()).updateProduct(any(Product.class), any(UpdateProductDomainDTO.class));
  }

  @Test
  @DisplayName("updateProductShouldNotCallFindProductByName - Should not call the findProductByName method if the productDTO name is null")
  void updateProductShouldNotCallFindProductByName() {
    Product product = this.productsDomain.getFirst();
    UpdateProductDomainDTO productDTO = new UpdateProductDTO(
      null,
      "anything",
      "anything",
      0L
    );

    when(this.productGateway.findProductById(product.getId())).thenReturn(Optional.of(product));
    when(this.productGateway.updateProduct(product, productDTO)).thenReturn(product);

    Product updatedProduct = this.updateProductUseCase.execute(product.getId(), productDTO);

    assertThat(updatedProduct.getId()).isEqualTo(product.getId());
    assertThat(updatedProduct.getName()).isEqualTo(product.getName());
    assertThat(updatedProduct.getDescription()).isEqualTo(product.getDescription());
    assertThat(updatedProduct.getUnitPrice().toString()).isEqualTo(product.getUnitPrice().toString());
    assertThat(updatedProduct.getQuantity()).isEqualTo(product.getQuantity());
    assertThat(updatedProduct.getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(updatedProduct.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(updatedProduct.getCategory()).usingRecursiveComparison().isEqualTo(product.getCategory());
    assertThat(updatedProduct.getBrand()).usingRecursiveComparison().isEqualTo(product.getBrand());
    assertThat(updatedProduct.getModel()).usingRecursiveComparison().isEqualTo(product.getModel());

    verify(this.productGateway, times(1)).findProductById(product.getId());
    verify(this.productGateway, never()).findProductByName(anyString());
    verify(this.productGateway, times(1)).updateProduct(product, productDTO);
  }

  private record UpdateProductDTO(String name,
                                  String description,
                                  String unitPrice,
                                  Long quantity) implements UpdateProductDomainDTO {
    public UpdateProductDTO(Product product) {
      this(
        product.getName(),
        product.getDescription(),
        product.getUnitPrice().toString(),
        product.getQuantity()
      );
    }
  }
}
