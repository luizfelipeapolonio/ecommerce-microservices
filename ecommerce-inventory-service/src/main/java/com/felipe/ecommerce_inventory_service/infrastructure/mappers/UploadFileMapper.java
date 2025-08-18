package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UploadFileImpl;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.MappingFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class UploadFileMapper {
  private final Logger logger = LoggerFactory.getLogger(UploadFileMapper.class);

  public UploadFile toUploadFile(MultipartFile file) {
    try {
      return UploadFileImpl.builder()
        .content(file.getBytes())
        .contentType(file.getContentType())
        .originalFileName(file.getOriginalFilename())
        .inputStream(file.getBytes())
        .size(file.getSize())
        .build();
    } catch(IOException ex) {
      this.logger.error("Error on mapping 'MultipartFile' to 'UploadFile': {}", ex.getMessage(), ex);
      throw new MappingFailureException("Não foi possível converter um arquivo em um componente interno", ex);
    }
  }

  public MultipartFile toMultipartFile(UploadFile file) {
    return new MultipartFile() {
      @Override
      public String getName() {
        return file.getOriginalName();
      }

      @Override
      public String getOriginalFilename() {
        return file.getOriginalName();
      }

      @Override
      public String getContentType() {
        return file.getContentType();
      }

      @Override
      public boolean isEmpty() {
        return false;
      }

      @Override
      public long getSize() {
        return file.getSize();
      }

      @Override
      public byte[] getBytes() throws IOException {
        return file.getContent();
      }

      @Override
      public InputStream getInputStream() throws IOException {
        return file.getInputStream();
      }

      @Override
      public void transferTo(File dest) throws IOException, IllegalStateException {
        try (FileOutputStream outputStream = new FileOutputStream(dest)) {
          outputStream.write(file.getContent());
        }
      }
    };
  }
}
