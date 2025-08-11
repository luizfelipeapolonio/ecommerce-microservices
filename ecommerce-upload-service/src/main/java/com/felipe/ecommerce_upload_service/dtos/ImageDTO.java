package com.felipe.ecommerce_upload_service.dtos;

import com.felipe.ecommerce_upload_service.persistence.entities.Image;
import com.felipe.ecommerce_upload_service.utils.ImageUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
  //private static final double UNIT_BYTE = Math.pow(1000, -2);

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

//  private static String convertByteToMegabyte(long bytes) {
//    double convertedMegabytes = UNIT_BYTE * bytes;
//    BigDecimal bigDecimalMegabytes = new BigDecimal(convertedMegabytes).setScale(2, RoundingMode.DOWN);
//    return String.format("%s MB", bigDecimalMegabytes.toString());
//  }
}
