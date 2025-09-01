package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;

import java.util.Optional;

public class GetProductsByBrandUseCaseImpl implements GetProductsByBrandUseCase {
  private final ProductGateway productGateway;
  private final BrandGateway brandGateway;

  public GetProductsByBrandUseCaseImpl(ProductGateway productGateway, BrandGateway brandGateway) {
    this.productGateway = productGateway;
    this.brandGateway = brandGateway;
  }

  @Override
  public PageResponseDTO execute(String brandName, int page, int elementsQuantity) {
    Optional<Brand> existingBrand = this.brandGateway.findBrandByName(brandName);
    if(existingBrand.isEmpty()) {
      throw new DataNotFoundException("Marca '" + brandName + "' não encontrada");
    }
    return this.productGateway.getProductsByBrand(brandName, page, elementsQuantity);
  }
}
