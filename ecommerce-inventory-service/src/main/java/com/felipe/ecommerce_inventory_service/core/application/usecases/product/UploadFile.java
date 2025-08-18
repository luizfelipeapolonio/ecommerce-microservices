package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import java.io.InputStream;

public interface UploadFile {
  byte[] getContent();
  String getContentType();
  String getOriginalName();
  InputStream getInputStream();
  long getSize();
}
