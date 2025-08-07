package com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ModelAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.UpdateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;

import java.util.Optional;

public class UpdateModelUseCaseImpl implements UpdateModelUseCase {
  private final ModelGateway modelGateway;

  public UpdateModelUseCaseImpl(ModelGateway modelGateway) {
    this.modelGateway = modelGateway;
  }

  @Override
  public Model execute(Long id, String name, String description) {
    Model model = this.modelGateway.findModelById(id)
      .orElseThrow(() -> new DataNotFoundException("Modelo de id '" + id + "' não encontrado"));

    Optional<Model> existingModel = this.modelGateway.findModelByName(name);
    if(existingModel.isPresent()) {
      throw new ModelAlreadyExistsException(name);
    }
    return this.modelGateway.updateModel(model, name, description);
  }
}
