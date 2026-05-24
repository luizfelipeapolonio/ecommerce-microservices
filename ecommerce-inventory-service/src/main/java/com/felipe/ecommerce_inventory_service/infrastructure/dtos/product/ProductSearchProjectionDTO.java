package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductSearchProjectionDTO {
  UUID getId();
  String getName();
  BigDecimal getUnitPrice();
  String getDiscountType();
  String getDiscountValue();
  long getQuantity();
  String getBrandName();
  String getCategoryName();
  String getModelName();
}
