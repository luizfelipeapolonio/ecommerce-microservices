package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public record ProductPageResponseDTO(int currentPage,
                                     int currentElements,
                                     int totalPages,
                                     long totalElements,
                                     List<ProductDTO> content) implements PageResponseDTO {
  public ProductPageResponseDTO(Page<ProductEntity> productsPage, List<ProductDTO> productDTOs) {
    this(
      productsPage.getNumber(),
      productsPage.getNumberOfElements(),
      productsPage.getTotalPages(),
      productsPage.getTotalElements(),
      productDTOs
    );
  }
}
