package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;

import java.util.Optional;

public interface BrandGateway {
  Brand createBrand(String name, String description);
  Optional<Brand> findBrandByName(String name);
}
