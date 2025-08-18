package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

public interface CreateProductDomainDTO {
  String name();
  String description();
  String unitPrice();
  Long quantity();
  Long categoryId();
  Long brandId();
  Long modelId();
}