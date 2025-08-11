package com.felipe.ecommerce_upload_service.config.storage;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {
  private final StorageProperties storageProperties;

  public ResourceConfiguration(StorageProperties storageProperties) {
    this.storageProperties = storageProperties;
  }

  // Add the 'uploads' folder in the root of the application as a static resource
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    final String uploadDir = this.storageProperties.getUploadDir();
    registry.addResourceHandler(String.format("/%s/**", uploadDir))
      .addResourceLocations(String.format("file:%s/", uploadDir));
  }
}
