package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

public interface UpdateProductDomainDTO {
  String name();
  String description();
  String unitPrice();
  Long quantity();
}
