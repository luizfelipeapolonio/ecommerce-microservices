package com.felipe.ecommerce_inventory_service.core.application.usecases.impl.brand;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.BrandAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.CreateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;

import java.util.Optional;

public class CreateBrandUseCaseImpl implements CreateBrandUseCase {
  private final BrandGateway brandGateway;

  public CreateBrandUseCaseImpl(BrandGateway brandGateway) {
    this.brandGateway = brandGateway;
  }

  @Override
  public Brand execute(String name, String description) {
    Optional<Brand> existingBrand = this.brandGateway.findBrandByName(name);
    if(existingBrand.isPresent()) {
      throw new BrandAlreadyExistsException(name);
    }
    return this.brandGateway.createBrand(name, description);
  }
}
