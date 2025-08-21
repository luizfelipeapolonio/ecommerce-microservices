package com.felipe.ecommerce_inventory_service.core.application.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductGateway {
  CreateProductResponseDTO createProduct(Product product, UploadFile[] files);
  Optional<Product> findProductByName(String name);
  Optional<Product> findProductById(UUID id);
  Product updateProduct(Product product, UpdateProductDomainDTO productDTO);
}
