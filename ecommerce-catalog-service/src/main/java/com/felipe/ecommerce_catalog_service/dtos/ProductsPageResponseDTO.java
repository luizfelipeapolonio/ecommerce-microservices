package com.felipe.ecommerce_catalog_service.dtos;

import com.felipe.response.product.ProductResponseDTO;

import java.util.List;

public record ProductsPageResponseDTO(int currentPage,
                                      int currentElements,
                                      int totalPages,
                                      long totalElements,
                                      List<ProductResponseDTO> content) {}
