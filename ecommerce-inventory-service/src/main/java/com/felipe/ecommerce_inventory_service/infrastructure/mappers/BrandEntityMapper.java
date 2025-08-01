package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import org.springframework.stereotype.Component;

@Component
public class BrandEntityMapper {
  public BrandEntity toEntity(Brand brand) {
    return BrandEntity.builder()
      .id(brand.getId())
      .name(brand.getName())
      .description(brand.getDescription())
      .createdAt(brand.getCreatedAt())
      .updatedAt(brand.getUpdatedAt())
      .build();
  }

  public Brand toDomain(BrandEntity entity) {
    return Brand.builder()
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .build();
  }
}
