package com.felipe.response.product;

public record ModelDTO(Long id, String name, String description, String createdAt, String updatedAt, BrandDTO brand) {
}
