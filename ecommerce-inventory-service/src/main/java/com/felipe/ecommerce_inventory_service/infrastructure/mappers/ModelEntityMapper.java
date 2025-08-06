package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import org.springframework.stereotype.Component;

@Component
public class ModelEntityMapper {
  private final BrandEntityMapper brandEntityMapper;

  public ModelEntityMapper(BrandEntityMapper brandEntityMapper) {
    this.brandEntityMapper = brandEntityMapper;
  }

  public Model toDomain(ModelEntity entity) {
    return Model.builder()
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .brand(this.brandEntityMapper.toDomain(entity.getBrand()))
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .build();
  }

  public ModelEntity toEntity(Model model) {
    return ModelEntity.builder()
      .id(model.getId())
      .name(model.getName())
      .description(model.getDescription())
      .brand(this.brandEntityMapper.toEntity(model.getBrand()))
      .createdAt(model.getCreatedAt())
      .updatedAt(model.getUpdatedAt())
      .build();
  }
}
