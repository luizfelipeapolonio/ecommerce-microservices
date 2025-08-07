package com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.DeleteModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;

public class DeleteModelUseCaseImpl implements DeleteModelUseCase {
  private final ModelGateway modelGateway;

  public DeleteModelUseCaseImpl(ModelGateway modelGateway) {
    this.modelGateway = modelGateway;
  }

  @Override
  public Model execute(Long id) {
    Model model = this.modelGateway.findModelById(id)
      .orElseThrow(() -> new DataNotFoundException("Modelo de id '" + id + "' não encontrado"));
    return this.modelGateway.deleteModel(model);
  }
}
