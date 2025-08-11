package com.felipe.ecommerce_upload_service.dtos;

import com.felipe.ecommerce_upload_service.persistence.entities.Image;
import com.felipe.ecommerce_upload_service.utils.ImageUtils;

public record ImageDTO(String id,
                       String name,
                       String path,
                       String fileType,
                       String fileSize,
                       String originalFileName,
                       String imageFor,
                       String productId,
                       String createdAt,
                       String updatedAt) {
  public ImageDTO(Image image) {
    this(
      image.getId().toString(),
      image.getName(),
      image.getPath(),
      image.getFileType(),
      ImageUtils.convertBytes(image.getFileSize()),
      image.getOriginalFileName(),
      image.getImageFor(),
      image.getProductId().toString(),
      image.getCreatedAt().toString(),
      image.getUpdatedAt().toString()
    );
  }
}
