package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ProductAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UpdateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.util.Optional;
import java.util.UUID;

public class UpdateProductUseCaseImpl implements UpdateProductUseCase {
  private final ProductGateway productGateway;

  public UpdateProductUseCaseImpl(ProductGateway productGateway) {
    this.productGateway = productGateway;
  }

  @Override
  public Product execute(UUID productId, UpdateProductDomainDTO productDTO) {
    Product product = this.productGateway.findProductById(productId)
      .orElseThrow(() -> new DataNotFoundException("Produto de id '" + productId + "' não encontrado"));

    if(productDTO.name() != null) {
      Optional<Product> existingProduct = this.productGateway.findProductByName(productDTO.name());
      if(existingProduct.isPresent()) {
        throw new ProductAlreadyExistsException(productDTO.name());
      }
    }
    return this.productGateway.updateProduct(product, productDTO);
  }
}
