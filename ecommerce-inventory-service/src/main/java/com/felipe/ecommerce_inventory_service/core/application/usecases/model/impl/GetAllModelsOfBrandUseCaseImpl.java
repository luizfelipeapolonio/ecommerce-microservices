package com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetAllModelsOfBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Model;

import java.util.List;

public class GetAllModelsOfBrandUseCaseImpl implements GetAllModelsOfBrandUseCase {
  private final ModelGateway modelGateway;
  private final GetBrandByIdUseCase getBrandByIdUseCase;

  public GetAllModelsOfBrandUseCaseImpl(ModelGateway modelGateway, GetBrandByIdUseCase getBrandByIdUseCase) {
    this.modelGateway = modelGateway;
    this.getBrandByIdUseCase = getBrandByIdUseCase;
  }

  @Override
  public List<Model> execute(Long brandId) {
    Brand foundBrand = this.getBrandByIdUseCase.execute(brandId);
    return this.modelGateway.findAllModelsByBrandId(foundBrand.getId());
  }
}