package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.InvalidProductQuantityException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.RemoveProductFromStockUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public class RemoveProductFromStockUseCaseImpl implements RemoveProductFromStockUseCase {
  private final ProductGateway productGateway;

  public RemoveProductFromStockUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public long execute(UUID productId, long quantity) {
    final Product product = this.productGateway.findProductById(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + productId + "' não encontrado"));

    if(quantity > product.getQuantity()) {
      throw new InvalidProductQuantityException(
        "Quantidade inválida! " +
        "A quantidade de produtos para remover do estoque não deve ser maior do que a quantidade atual de produtos disponíveis"
      );
    }
    final Product updatedProduct = Product.mutate(product)
      .quantity(product.getQuantity() - quantity)
      .build();

    return this.productGateway.updateProductQuantityInStock(updatedProduct);
  }
}
