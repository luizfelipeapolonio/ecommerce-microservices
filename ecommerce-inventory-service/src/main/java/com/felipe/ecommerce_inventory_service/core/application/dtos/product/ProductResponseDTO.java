package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

import java.util.List;

public interface ProductResponseDTO {
  String id();
  String name();
  String description();
  String unitPrice();
  long quantity();
  String createdAt();
  String updatedAt();
  List<ImageFileDTO> images();
}
