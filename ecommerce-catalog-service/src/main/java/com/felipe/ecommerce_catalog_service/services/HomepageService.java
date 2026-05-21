package com.felipe.ecommerce_catalog_service.services;

import com.felipe.ecommerce_catalog_service.dtos.HomepageProductsDTO;
import com.felipe.ecommerce_catalog_service.dtos.ProductsPageResponseDTO;
import com.felipe.ecommerce_catalog_service.external.InventoryServiceClient;
import com.felipe.response.ResponsePayload;
import com.felipe.response.product.CategoryDTO;
import com.felipe.response.product.ProductResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class HomepageService {
  private final RedisTemplate<String, HomepageProductsDTO> redisTemplate;
  private final CategoryRegistryService categoryRegistryService;
  private final InventoryServiceClient inventoryServiceClient;
  private static final String CACHE_KEY = "catalog:homepage";
  private static final Logger logger = LoggerFactory.getLogger(HomepageService.class);

  public HomepageService(RedisTemplate<String, HomepageProductsDTO> redisTemplate, CategoryRegistryService categoryRegistryService,
                         InventoryServiceClient inventoryServiceClient) {
    this.redisTemplate = redisTemplate;
    this.categoryRegistryService = categoryRegistryService;
    this.inventoryServiceClient = inventoryServiceClient;
  }

  public HomepageProductsDTO getHomepageProducts() {
    HomepageProductsDTO cached = this.redisTemplate.opsForValue().get(CACHE_KEY);
    if (cached != null) {
      logger.info("Returning cached products");
      return cached;
    }

    // Emergency fallback
    // Block thread to get homepage products
    logger.warn("Homepage cache missing. Rebuilding synchronously");
    HomepageProductsDTO rebuilt = generateHomepageProducts().join();
    saveCache(rebuilt);
    return rebuilt;
  }

  public CompletableFuture<Void> initialize() {
    logger.info("Initializing homepage cache");
    return generateHomepageProducts().thenAccept(this::saveCache);
  }

  // Refresh homepage every 15 minutes in a non-blocking way
  @Scheduled(cron = "0 */15 * * * *")
  public void refreshHomepageProducts() {
    CronExpression cron = CronExpression.parse("0 */15 * * * *");
    LocalDateTime nextExecution = cron.next(LocalDateTime.now());
    logger.info("Refreshing homepage products. Next refresh -> {}", nextExecution);

    generateHomepageProducts()
      .thenAccept(this::saveCache)
      .exceptionally(exception -> {
        logger.error("Failed to refresh homepage cache. Message -> {}", exception.getMessage(), exception);
        return null;
      });
  }

  private CompletableFuture<HomepageProductsDTO> generateHomepageProducts() {
    List<CategoryDTO> categories = this.categoryRegistryService.getRandomCategories();
    List<CompletableFuture<ProductsPageResponseDTO>> futures = categories
      .stream()
      .map(category -> this.inventoryServiceClient.fetchProducts(category.name())
          .thenApply(ResponsePayload::getPayload))
      .toList();

    CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));

    // Process results in a non-blocking way when all are complete
    return allFutures.thenApply(unused -> {
      logger.info("Processing all futures");
      List<ProductResponseDTO> products = futures.stream()
        .map(CompletableFuture::join)
        .flatMap(productsPageResponseDTO -> productsPageResponseDTO.content().stream())
        .toList();
      return new HomepageProductsDTO(products.size(), products);
    });
  }

  private void saveCache(HomepageProductsDTO products) {
    this.redisTemplate.opsForValue().set(CACHE_KEY, products);
    logger.info("Homepage cache updated successfully");
  }
}
