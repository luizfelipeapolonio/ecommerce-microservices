package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import java.util.UUID;

public interface AddProductInStockUseCase {
  long execute(UUID productId, long quantity);
}
