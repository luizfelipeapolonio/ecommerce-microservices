package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;

public interface ApplyPromotionToProductsUseCase {
  int execute(PromotionDTO promotionDTO);
}
