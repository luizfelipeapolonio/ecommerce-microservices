package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.AddProductInStockUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
public class AddProductInStockUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private AddProductInStockUseCaseImpl addProductInStockUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.addProductInStockUseCase = new AddProductInStockUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("addProductInStockSuccess - Should successfully add product in stock increasing the product quantity")
  void addProductInStockSuccess() {
    final Product productDomain = Product.mutate(this.productsDomain.getFirst())
      .quantity(10)
      .build();
    final long productQuantityToAdd = 10L;
    final ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

    when(this.productGateway.findProductById(productDomain.getId())).thenReturn(Optional.of(productDomain));
    when(this.productGateway.updateProductQuantityInStock(productCaptor.capture())).thenReturn(20L);

    long currentProductQuantity = this.addProductInStockUseCase.execute(productDomain.getId(), productQuantityToAdd);

    assertThat(productCaptor.getValue().getQuantity()).isEqualTo(20);
    assertThat(currentProductQuantity).isEqualTo(20);

    verify(this.productGateway, times(1)).findProductById(productDomain.getId());
    verify(this.productGateway, times(1)).updateProductQuantityInStock(any(Product.class));
  }

  @Test
  @DisplayName("addProductInStockFailsByProductNotFound - Should throw a DataNotFoundException if the product is not found")
  void addProductInStockFailsByProductNotFound() {
    final UUID productId = this.productsDomain.getFirst().getId();

    when(this.productGateway.findProductById(productId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.addProductInStockUseCase.execute(productId, 1L));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id: '%s' não encontrado", productId);

    verify(this.productGateway, times(1)).findProductById(productId);
    verify(this.productGateway, never()).updateProductQuantityInStock(any(Product.class));
  }
}
