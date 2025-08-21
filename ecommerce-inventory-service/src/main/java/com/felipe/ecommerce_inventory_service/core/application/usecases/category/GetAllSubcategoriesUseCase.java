package com.felipe.ecommerce_inventory_service.core.application.usecases.category;

import com.felipe.ecommerce_inventory_service.core.domain.Category;

import java.util.List;

public interface GetAllSubcategoriesUseCase {
  List<Category> execute();
}
