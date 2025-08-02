package com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;

public class GetBrandByIdUseCaseImpl implements GetBrandByIdUseCase {
  private final BrandGateway brandGateway;

  public GetBrandByIdUseCaseImpl(BrandGateway brandGateway) {
    this.brandGateway = brandGateway;
  }

  @Override
  public Brand execute(Long id) {
    return this.brandGateway.findBrandById(id)
      .orElseThrow(() -> new DataNotFoundException("Marca de id '" + id + "' não encontrada"));
  }
}
