package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;

public interface CreateProductUseCase {
  CreateProductResponseDTO execute(CreateProductDomainDTO productDTO, UploadFile[] files);
}
