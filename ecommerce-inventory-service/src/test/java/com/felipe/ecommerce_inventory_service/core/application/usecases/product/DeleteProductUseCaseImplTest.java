package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.DeleteProductUseCaseImpl;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class DeleteProductUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private DeleteProductUseCaseImpl deleteProductUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.deleteProductUseCase = new DeleteProductUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("deleteProductSuccess - Should successfully delete a product and return it")
  void deleteProductSuccess() {
    final Product product = this.productsDomain.getFirst();

    when(this.productGateway.findProductById(product.getId())).thenReturn(Optional.of(product));
    when(this.productGateway.deleteProduct(product)).thenReturn(product);

    Product deletedProduct = this.deleteProductUseCase.execute(product.getId());

    assertThat(deletedProduct.getId()).isEqualTo(product.getId());
    assertThat(deletedProduct.getName()).isEqualTo(product.getName());
    assertThat(deletedProduct.getDescription()).isEqualTo(product.getDescription());
    assertThat(deletedProduct.getQuantity()).isEqualTo(product.getQuantity());
    assertThat(deletedProduct.getUnitPrice().toString()).isEqualTo(product.getUnitPrice().toString());
    assertThat(deletedProduct.getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(deletedProduct.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(deletedProduct.getCategory()).usingRecursiveComparison().isEqualTo(product.getCategory());
    assertThat(deletedProduct.getBrand()).usingRecursiveComparison().isEqualTo(product.getBrand());
    assertThat(deletedProduct.getModel()).usingRecursiveComparison().isEqualTo(product.getModel());

    verify(this.productGateway, times(1)).findProductById(product.getId());
    verify(this.productGateway, times(1)).deleteProduct(product);
  }

  @Test
  @DisplayName("deleteProductFailsByProductNotFound - Should throw a DataNotFoundException if the product is not found")
  void deleteProductFailsByProductNotFound() {
    final UUID productId = this.productsDomain.getFirst().getId();

    when(this.productGateway.findProductById(productId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deleteProductUseCase.execute(productId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id: '%s' não encontrado", productId);

    verify(this.productGateway, times(1)).findProductById(productId);
    verify(this.productGateway, never()).deleteProduct(any(Product.class));
  }
}
