package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductInStockDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.CheckIfProductIsInStockUseCaseImpl;
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

@ExtendWith(MockitoExtension.class)
public class CheckIfProductIsInStockUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private CheckIfProductIsInStockUseCaseImpl checkIfProductIsInStockUseCase;
  private List<Product> productsDomain;

  @BeforeEach
  void setUp() {
    this.checkIfProductIsInStockUseCase = new CheckIfProductIsInStockUseCaseImpl(this.productGateway);
    this.productsDomain = new DataMock().getProductsDomain();
  }

  @Test
  @DisplayName("checkIfProductIsInStockReturnsTrue - Should successfully return true if the product is in stock")
  void checkIfProductIsInStockReturnsTrue() {
    final Product productDomain = this.productsDomain.getFirst();

    when(this.productGateway.findProductById(productDomain.getId())).thenReturn(Optional.of(productDomain));

    ProductInStockDTO product = this.checkIfProductIsInStockUseCase.execute(productDomain.getId());

    assertThat(product.isInStock()).isTrue();
    verify(this.productGateway, times(1)).findProductById(productDomain.getId());
  }

  @Test
  @DisplayName("checkIfProductIsInStockReturnsFalse - Should successfully return false if the product is not in stock")
  void checkIfProductIsInStockReturnsFalse() {
    final Product productDomain = Product.mutate(this.productsDomain.getFirst())
      .quantity(0)
      .build();

    when(this.productGateway.findProductById(productDomain.getId())).thenReturn(Optional.of(productDomain));

    ProductInStockDTO product = this.checkIfProductIsInStockUseCase.execute(productDomain.getId());

    assertThat(product.isInStock()).isFalse();
    verify(this.productGateway, times(1)).findProductById(productDomain.getId());
  }

  @Test
  @DisplayName("checkIfProductIsInStockFailsByProductNotFound - Should throw a DataNotFoundException if the product is not found")
  void checkIfProductIsInStockFailsByProductNotFound() {
    final UUID productId = this.productsDomain.getFirst().getId();

    when(this.productGateway.findProductById(productId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.checkIfProductIsInStockUseCase.execute(productId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Produto de id: '%s' não encontrado", productId);

    verify(this.productGateway, times(1)).findProductById(productId);
  }
}
