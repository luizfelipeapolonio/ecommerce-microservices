package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

import java.util.List;
import java.util.UUID;

public record SearchProductsResponseDTO(int currentPage,
                                        int currentElements,
                                        int totalPages,
                                        long totalElements,
                                        List<SearchedProducts> products) {
  public record SearchedProducts(UUID id,
                                 String productName,
                                 String unitPrice,
                                 String discountType,
                                 String discountValue,
                                 String finalPrice,
                                 long quantity,
                                 String brand,
                                 String category,
                                 String model,
                                 String thumbnailPath) {}
}
