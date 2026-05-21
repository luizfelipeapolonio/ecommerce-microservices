package com.felipe.ecommerce_catalog_service.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class StartupOrchestratorService {
  private final CategoryRegistryService categoryRegistryService;
  private final HomepageService homepageService;
  private static final Logger logger = LoggerFactory.getLogger(StartupOrchestratorService.class);

  public StartupOrchestratorService(CategoryRegistryService categoryRegistryService, HomepageService homepageService) {
    this.categoryRegistryService = categoryRegistryService;
    this.homepageService = homepageService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void bootstrap() {
    logger.info("Starting application bootstrap orchestrator");
    this.categoryRegistryService.initialize()
      .thenCompose(unused -> this.homepageService.initialize())
      .thenRun(() -> logger.info("Application bootstrap completed"))
      .exceptionally(exception -> {
        logger.error("Application bootstrap failed -> Message: {}", exception.getMessage(), exception);
        return null;
      });
  }
}
