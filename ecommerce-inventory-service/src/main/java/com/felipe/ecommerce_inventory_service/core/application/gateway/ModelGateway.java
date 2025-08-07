package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.Model;

import java.util.List;
import java.util.Optional;

public interface ModelGateway {
  Model createModel(Model model);
  Optional<Model> findModelByName(String name);
  Optional<Model> findModelById(Long id);
  List<Model> findAllModelsByBrandId(Long brandId);
  Model updateModel(Model model, String name, String description);
}
