package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductInStockDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CheckIfProductIsInStockUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public class CheckIfProductIsInStockUseCaseImpl implements CheckIfProductIsInStockUseCase {
  private final ProductGateway productGateway;

  public CheckIfProductIsInStockUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public ProductInStockDTO execute(UUID productId) {
    final Product product = this.productGateway.findProductById(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + productId + "' não encontrado"));
    return () -> product.getQuantity() > 0;
  }
}
