package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.DeleteProductUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public class DeleteProductUseCaseImpl implements DeleteProductUseCase {
  private final ProductGateway productGateway;

  public DeleteProductUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public Product execute(UUID productId) {
    Product product = this.productGateway.findProductById(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + productId + "' não encontrado"));
    return this.productGateway.deleteProduct(product);
  }
}
