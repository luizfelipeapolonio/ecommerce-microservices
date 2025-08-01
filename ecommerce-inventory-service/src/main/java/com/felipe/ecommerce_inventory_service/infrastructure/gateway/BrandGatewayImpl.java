package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.BrandEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.BrandRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BrandGatewayImpl implements BrandGateway {
  private final BrandRepository brandRepository;
  private final BrandEntityMapper brandEntityMapper;

  public BrandGatewayImpl(BrandRepository brandRepository, BrandEntityMapper brandEntityMapper) {
    this.brandRepository = brandRepository;
    this.brandEntityMapper = brandEntityMapper;
  }

  @Override
  public Brand createBrand(String name, String description) {
    BrandEntity brand = BrandEntity.builder().name(name).description(description).build();
    return this.brandEntityMapper.toDomain(this.brandRepository.save(brand));
  }

  @Override
  public Optional<Brand> findBrandByName(String name) {
    return this.brandRepository.findByName(name).map(this.brandEntityMapper::toDomain);
  }
}
