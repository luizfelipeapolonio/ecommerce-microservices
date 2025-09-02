package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;

import java.util.Optional;

public class GetProductsByModelUseCaseImpl implements GetProductsByModelUseCase {
  private final ProductGateway productGateway;
  private final ModelGateway modelGateway;

  public GetProductsByModelUseCaseImpl(ProductGateway productGateway, ModelGateway modelGateway) {
    this.productGateway = productGateway;
    this.modelGateway = modelGateway;
  }

  @Override
  public PageResponseDTO execute(String modelName, String brandName, int page, int elementsQuantity) {
    Optional<Model> existingModel = this.modelGateway.findModelByNameAndBrandName(modelName, brandName);
    if(existingModel.isEmpty()) {
      throw new DataNotFoundException("Modelo '" + modelName + "' da marca '" + brandName + "' não encontrado");
    }
    return this.productGateway.getProductsByModel(modelName, brandName, page, elementsQuantity);
  }
}
