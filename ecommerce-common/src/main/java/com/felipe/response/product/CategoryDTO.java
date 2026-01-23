package com.felipe.response.product;

public record CategoryDTO(Long id, String name, String createdAt, String updatedAt, CategoryDTO parentCategory) {
}
