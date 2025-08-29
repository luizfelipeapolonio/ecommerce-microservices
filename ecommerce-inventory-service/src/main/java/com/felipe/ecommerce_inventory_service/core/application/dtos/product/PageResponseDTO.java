package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

import java.util.List;

public interface PageResponseDTO {
  int currentPage();
  int currentElements();
  int totalPages();
  long totalElements();
  List<? extends ProductResponseDTO> content();
}
