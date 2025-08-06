package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.domain.Model;

import java.util.List;

public interface GetAllModelsOfBrandUseCase {
  List<Model> execute(Long brandId);
}
