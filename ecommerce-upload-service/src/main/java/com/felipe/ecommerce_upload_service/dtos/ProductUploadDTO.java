package com.felipe.ecommerce_upload_service.dtos;

import java.util.UUID;

public record ProductUploadDTO(String productName, UUID productId) {
  public ProductUploadDTO {
    productName = productName.trim().replaceAll(" ", "_");
  }
}
