package com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.DeleteBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;

public class DeleteBrandUseCaseImpl implements DeleteBrandUseCase {
  private final BrandGateway brandGateway;

  public DeleteBrandUseCaseImpl(BrandGateway brandGateway) {
    this.brandGateway = brandGateway;
  }

  @Override
  public Brand execute(Long id) {
    Brand brand = this.brandGateway.findBrandById(id)
      .orElseThrow(() -> new DataNotFoundException("Marca de id '" + id + "' não encontrada"));
    return this.brandGateway.deleteBrand(brand);
  }
}
