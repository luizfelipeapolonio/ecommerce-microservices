package com.felipe.ecommerce_upload_service.config.storage;

import com.felipe.ecommerce_upload_service.exceptions.UploadDirectoryInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class StorageConfiguration {
  private final Path imageStorageLocation;
  private final Logger logger = LoggerFactory.getLogger(StorageConfiguration.class);

  public StorageConfiguration(StorageProperties storageProperties) {
    this.imageStorageLocation = Paths.get(storageProperties.getUploadDir())
      .toAbsolutePath()
      .normalize();
  }

  public void init() {
    try {
      if (Files.exists(this.imageStorageLocation)) return;
      final Path root = Files.createDirectory(this.imageStorageLocation);
      this.logger.info("Upload directory successfully created on: {}", root.toAbsolutePath());
    } catch(IOException ex) {
      this.logger.error("Error on upload directory creation: {}", ex.getMessage(), ex);
      throw new UploadDirectoryInitializationException(ex);
    }
  }
}
