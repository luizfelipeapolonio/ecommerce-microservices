package com.felipe.ecommerce_catalog_service.services;

import com.felipe.ecommerce_catalog_service.external.InventoryServiceClient;
import com.felipe.response.ResponsePayload;
import com.felipe.response.product.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
public class CategoryRegistryService {
  private final InventoryServiceClient inventoryServiceClient;
  private final List<CategoryDTO> categories = new CopyOnWriteArrayList<>();
  private static final Logger logger = LoggerFactory.getLogger(CategoryRegistryService.class);

  public CategoryRegistryService(InventoryServiceClient inventoryServiceClient) {
    this.inventoryServiceClient = inventoryServiceClient;
  }

  public CompletableFuture<Void> initialize() {
    return this.inventoryServiceClient.fetchCategories()
      .thenAccept(response -> {
        this.categories.addAll(response.getPayload());
        logger.info("Initializing categories. Added {} new categories", this.categories.size());
      });
  }

  @Scheduled(cron = "0 0 0 * * *") // Look for new categories every day
  public void refreshCategories() {
    CronExpression cron = CronExpression.parse("0 0 0 * * *");
    LocalDateTime nextExecution = cron.next(LocalDateTime.now());
    logger.info("Refreshing categories. Next refresh -> {}", nextExecution);

    this.inventoryServiceClient.fetchCategories()
      .thenAccept(this::mergeCategories)
      .exceptionally(exception -> {
        logger.error("Error in refreshing categories -> Message: {}", exception.getMessage(), exception);
        return null;
      });
  }

  public List<CategoryDTO> getRandomCategories() {
    List<CategoryDTO> shuffled = new ArrayList<>(this.categories);
    Collections.shuffle(shuffled);
    return shuffled.stream().limit(5).toList();
  }

  private void mergeCategories(ResponsePayload<List<CategoryDTO>> fetched) {
    logger.info("Looking for new categories to be merged");
    List<CategoryDTO> fetchedCategories = fetched.getPayload();
    Set<Long> existingIds = this.categories.stream().map(CategoryDTO::id).collect(Collectors.toSet());
    List<CategoryDTO> newCategories = fetchedCategories
      .stream()
      .filter(category -> !existingIds.contains(category.id()))
      .toList();
    if (newCategories.isEmpty()) return;

    this.categories.addAll(newCategories);
    logger.info("Added {} new categories", newCategories.size());
  }
}
