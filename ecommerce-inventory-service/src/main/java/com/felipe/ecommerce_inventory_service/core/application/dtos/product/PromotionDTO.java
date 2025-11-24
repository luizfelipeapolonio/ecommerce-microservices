package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PromotionDTO(
  String promotionId,
  String promotionScope,
  String discountType,
  String discountValue,
  LocalDateTime endDate,
  BigDecimal minimumPrice,
  List<PromotionAppliesToDTO> targets
) {}
