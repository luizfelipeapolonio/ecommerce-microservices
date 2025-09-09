package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.InvalidProductQuantityException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.RemoveProductFromStockUseCaseImpl;
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
public class RemoveProductFromStockUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private RemoveProductFromStockUseCaseImpl removeProductFromStockUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.removeProductFromStockUseCase = new RemoveProductFromStockUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("removeProductFromStockSuccess - Should successfully remove product from stock and decrease the product quantity")
  void removeProductFromStockSuccess() {
    final Product productDomain = Product.mutate(this.productsDomain.getFirst())
      .quantity(10)
      .build();
    final long productQuantityToRemove = 1L;
    final ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

    when(this.productGateway.findProductById(productDomain.getId())).thenReturn(Optional.of(productDomain));
    when(this.productGateway.updateProductQuantityInStock(productCaptor.capture())).thenReturn(9L);

    long currentProductQuantity = this.removeProductFromStockUseCase.execute(productDomain.getId(), productQuantityToRemove);

    assertThat(productCaptor.getValue().getQuantity()).isEqualTo(9);
    assertThat(currentProductQuantity).isEqualTo(9);

    verify(this.productGateway, times(1)).findProductById(productDomain.getId());
    verify(this.productGateway, times(1)).updateProductQuantityInStock(any(Product.class));
  }

  @Test
  @DisplayName("removeProductFromStockFailsByInvalidQuantity - Should throw an InvalidProductQuantityException if the quantity to remove is invalid")
  void removeProductFromStockFailsByInvalidQuantity() {
    final Product productDomain = Product.mutate(this.productsDomain.getFirst())
      .quantity(10)
      .build();
    final long productQuantityToRemove = 11L;

    when(this.productGateway.findProductById(productDomain.getId())).thenReturn(Optional.of(productDomain));

    Exception thrown = catchException(() -> this.removeProductFromStockUseCase.execute(productDomain.getId(), productQuantityToRemove));

    assertThat(thrown)
      .isExactlyInstanceOf(InvalidProductQuantityException.class)
      .hasMessage(
        "Quantidade inválida! " +
        "A quantidade de produtos para remover do estoque não deve ser maior do que a quantidade atual de produtos disponíveis"
      );

    verify(this.productGateway, times(1)).findProductById(productDomain.getId());
    verify(this.productGateway, never()).updateProductQuantityInStock(any(Product.class));
  }

  @Test
  @DisplayName("removeProductFromStockFailsByProductNotFound - Should throw a DataNotFoundException if the product is not found")
  void removeProductFromStockFailsByProductNotFound() {
    final UUID productId = this.productsDomain.getFirst().getId();

    when(this.productGateway.findProductById(productId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.removeProductFromStockUseCase.execute(productId, 1L));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id: '%s' não encontrado", productId);

    verify(this.productGateway, times(1)).findProductById(productId);
    verify(this.productGateway, never()).updateProductQuantityInStock(any(Product.class));
  }
}
