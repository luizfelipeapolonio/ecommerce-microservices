package com.felipe.response.product;

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

