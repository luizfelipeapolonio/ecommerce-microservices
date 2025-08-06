package com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;

public class GetModelByIdUseCaseImpl implements GetModelByIdUseCase {
  private final ModelGateway modelGateway;

  public GetModelByIdUseCaseImpl(ModelGateway modelGateway) {
    this.modelGateway = modelGateway;
  }

  @Override
  public Model execute(Long id) {
    return this.modelGateway.findModelById(id)
      .orElseThrow(() -> new DataNotFoundException("Modelo de id '" + id + "' não encontrado"));
  }
}
