package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.BrandEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.BrandRepository;
import org.springframework.stereotype.Component;

import java.util.List;
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

  @Override
  public Optional<Brand> findBrandById(Long id) {
    return this.brandRepository.findById(id).map(this.brandEntityMapper::toDomain);
  }

  @Override
  public List<Brand> getAllBrands() {
    return this.brandRepository.findAll().stream().map(this.brandEntityMapper::toDomain).toList();
  }

  @Override
  public Brand updateBrand(Brand brand, String name, String description) {
    BrandEntity.Builder brandBuilder = BrandEntity.mutate(this.brandEntityMapper.toEntity(brand));
    if(name != null) {
      brandBuilder.name(name);
    }
    if(description != null) {
      brandBuilder.description(description);
    }
    BrandEntity updatedBrand = this.brandRepository.save(brandBuilder.build());
    return this.brandEntityMapper.toDomain(updatedBrand);
  }

  @Override
  public Brand deleteBrand(Brand brand) {
    BrandEntity brandEntity = this.brandEntityMapper.toEntity(brand);
    this.brandRepository.delete(brandEntity);
    return brand;
  }
}
