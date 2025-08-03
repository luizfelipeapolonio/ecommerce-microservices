package com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.BrandAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.UpdateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;

import java.util.Optional;

public class UpdateBrandUseCaseImpl implements UpdateBrandUseCase {
  private final BrandGateway brandGateway;

  public UpdateBrandUseCaseImpl(BrandGateway brandGateway) {
    this.brandGateway = brandGateway;
  }

  @Override
  public Brand execute(Long id, String name, String description) {
    Brand brand = this.brandGateway.findBrandById(id)
      .orElseThrow(() -> new DataNotFoundException("Marca de id '" + id + "' não encontrada"));

    Optional<Brand> existingBrand = this.brandGateway.findBrandByName(name);
    if(existingBrand.isPresent()) {
      throw new BrandAlreadyExistsException(name);
    }
    return this.brandGateway.updateBrand(brand, name, description);
  }
}
