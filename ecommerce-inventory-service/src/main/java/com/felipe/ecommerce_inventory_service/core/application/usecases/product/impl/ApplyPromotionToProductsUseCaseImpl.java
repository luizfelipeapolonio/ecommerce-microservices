package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.ApplyPromotionToProductsUseCase;

public class ApplyPromotionToProductsUseCaseImpl implements ApplyPromotionToProductsUseCase {
  private final ProductGateway productGateway;

  public ApplyPromotionToProductsUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public int execute(PromotionDTO promotionDTO) {
    return this.productGateway.applyPromotionToProducts(promotionDTO);
  }
}
