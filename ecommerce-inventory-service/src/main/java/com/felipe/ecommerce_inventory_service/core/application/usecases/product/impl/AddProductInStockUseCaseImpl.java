package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.AddProductInStockUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public class AddProductInStockUseCaseImpl implements AddProductInStockUseCase {
  private final ProductGateway productGateway;

  public AddProductInStockUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public long execute(UUID productId, long quantity) {
    final Product product = this.productGateway.findProductById(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + productId + "' não encontrado"));
    final Product updatedProduct = Product.mutate(product)
      .quantity(product.getQuantity() + quantity)
      .build();

    return this.productGateway.updateProductQuantityInStock(updatedProduct);
  }
}
