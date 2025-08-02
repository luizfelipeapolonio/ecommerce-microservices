package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;

import java.util.List;

public interface GetAllBrandsUseCase {
  List<Brand> execute();
}
