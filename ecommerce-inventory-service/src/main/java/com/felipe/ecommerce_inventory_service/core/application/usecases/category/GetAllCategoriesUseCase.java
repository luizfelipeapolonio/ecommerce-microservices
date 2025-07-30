package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.application.dtos.CategoriesDTO;

import java.util.List;

public interface GetAllCategoriesUseCase {
  List<CategoriesDTO> execute();
}
