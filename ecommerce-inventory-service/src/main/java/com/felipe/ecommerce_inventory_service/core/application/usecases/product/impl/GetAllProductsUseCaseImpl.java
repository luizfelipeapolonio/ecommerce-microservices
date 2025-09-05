package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetAllProductsUseCase;

public class GetAllProductsUseCaseImpl implements GetAllProductsUseCase {
  private final ProductGateway productGateway;

  public GetAllProductsUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public PageResponseDTO execute(int page, int elementsQuantity) {
    return this.productGateway.getAllProducts(page, elementsQuantity);
  }
}
