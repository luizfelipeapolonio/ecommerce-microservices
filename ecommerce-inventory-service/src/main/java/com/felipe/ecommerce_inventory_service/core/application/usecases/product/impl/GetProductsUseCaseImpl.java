package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsUseCase;

public class GetProductsUseCaseImpl implements GetProductsUseCase {
  private final ProductGateway productGateway;

  public GetProductsUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public PageResponseDTO execute(String category, String brand, String model, int page, int elementsQuantity) {
    return this.productGateway.getProducts(category, brand, model, page, elementsQuantity);
  }
}
