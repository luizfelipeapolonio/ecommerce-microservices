package com.felipe.ecommerce_inventory_service.core.application.dtos.product;

public record ImageFileDTO(String id,
                           String name,
                           String path,
                           String fileType,
                           String fileSize,
                           String originalFileName,
                           String imageFor,
                           String productId,
                           String createdAt,
                           String updatedAt) {
}
