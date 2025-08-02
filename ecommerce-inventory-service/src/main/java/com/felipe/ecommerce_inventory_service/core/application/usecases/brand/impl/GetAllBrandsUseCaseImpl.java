package com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetAllBrandsUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;

import java.util.List;

public class GetAllBrandsUseCaseImpl implements GetAllBrandsUseCase {
  private final BrandGateway brandGateway;

  public GetAllBrandsUseCaseImpl(BrandGateway brandGateway) {
    this.brandGateway = brandGateway;
  }

  @Override
  public List<Brand> execute() {
    return this.brandGateway.getAllBrands();
  }
}
