package com.felipe.ecommerce_catalog_service.dtos;

import com.felipe.response.product.ProductResponseDTO;

import java.util.List;

public record HomepageProductsDTO(int totalElements, List<ProductResponseDTO> products) {}
