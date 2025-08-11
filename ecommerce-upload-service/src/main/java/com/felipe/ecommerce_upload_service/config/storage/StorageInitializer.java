package com.felipe.ecommerce_upload_service.config.storage;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageInitializer {

  @Bean
  public CommandLineRunner initialize(StorageConfiguration storageConfiguration) {
    return args -> storageConfiguration.init();
  }
}
