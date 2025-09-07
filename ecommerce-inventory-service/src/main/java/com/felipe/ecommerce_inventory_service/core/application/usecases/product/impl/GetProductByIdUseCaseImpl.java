package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.UUID;

public class GetProductByIdUseCaseImpl implements GetProductByIdUseCase {
  private final ProductGateway productGateway;

  public GetProductByIdUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public ProductResponseDTO execute(UUID productId) {
    Product product = this.productGateway.findProductById(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id: '" + productId + "' não encontrado"));
    return this.productGateway.getProduct(product);
  }
}
