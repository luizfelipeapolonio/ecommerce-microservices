package com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.ModelAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Model;

import java.util.Optional;

public class CreateModelUseCaseImpl implements CreateModelUseCase {
  private final ModelGateway modelGateway;
  private final GetBrandByIdUseCase getBrandByIdUseCase;

  public CreateModelUseCaseImpl(ModelGateway modelGateway, GetBrandByIdUseCase getBrandByIdUseCase) {
    this.modelGateway = modelGateway;
    this.getBrandByIdUseCase = getBrandByIdUseCase;
  }

  @Override
  public Model execute(String name, String description, Long brandId) {
    Brand brand = this.getBrandByIdUseCase.execute(brandId);

    Optional<Model> existingModel = this.modelGateway.findModelByName(name);
    if(existingModel.isPresent()) {
      throw new ModelAlreadyExistsException(name);
    }

    Model model = Model.builder()
      .name(name)
      .description(description)
      .brand(brand)
      .build();

    return this.modelGateway.createModel(model);
  }
}
