package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductEntityMapper {
  private final CategoryEntityMapper categoryEntityMapper;
  private final BrandEntityMapper brandEntityMapper;
  private final ModelEntityMapper modelEntityMapper;

  public ProductEntityMapper(CategoryEntityMapper categoryEntityMapper, BrandEntityMapper brandEntityMapper,
                             ModelEntityMapper modelEntityMapper) {
    this.categoryEntityMapper = categoryEntityMapper;
    this.brandEntityMapper = brandEntityMapper;
    this.modelEntityMapper = modelEntityMapper;
  }

  public ProductEntity toEntity(Product product) {
    return ProductEntity.builder()
      .id(product.getId())
      .name(product.getName())
      .description(product.getDescription())
      .unitPrice(product.getUnitPrice())
      .quantity(product.getQuantity())
      .withDiscount(product.isItWithDiscount())
      .discountType(product.getDiscountType())
      .discountValue(product.getDiscountValue())
      .createdAt(product.getCreatedAt())
      .updatedAt(product.getUpdatedAt())
      .category(this.categoryEntityMapper.toEntity(product.getCategory()))
      .brand(this.brandEntityMapper.toEntity(product.getBrand()))
      .model(this.modelEntityMapper.toEntity(product.getModel()))
      .build();
  }

  public Product toDomain(ProductEntity entity) {
    return Product.builder()
      .id(entity.getId())
      .name(entity.getName())
      .description(entity.getDescription())
      .unitPrice(entity.getUnitPrice())
      .quantity(entity.getQuantity())
      .withDiscount(entity.isItWithDiscount())
      .discountType(entity.getDiscountType())
      .discountValue(entity.getDiscountValue())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt())
      .category(this.categoryEntityMapper.toDomain(entity.getCategory()))
      .brand(this.brandEntityMapper.toDomain(entity.getBrand()))
      .model(this.modelEntityMapper.toDomain(entity.getModel()))
      .build();
  }
}
