package com.felipe.ecommerce_catalog_service.external;

import com.felipe.ecommerce_catalog_service.dtos.ProductsPageResponseDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.product.CategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class InventoryServiceClient {
  private final InventoryServiceProperties inventoryServiceProperties;
  private final RestClient restClient;
  private static final Logger logger = LoggerFactory.getLogger(InventoryServiceClient.class);
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-catalog-service";

  public InventoryServiceClient(InventoryServiceProperties inventoryServiceProperties, RestClient restClient) {
    this.inventoryServiceProperties = inventoryServiceProperties;
    this.restClient = restClient;
  }

  @Async
  public CompletableFuture<ResponsePayload<ProductsPageResponseDTO>> fetchProducts(String categoryName) {
    ResponsePayload<ProductsPageResponseDTO> response = this.restClient
      .get()
      .uri(uriBuilder -> uriBuilder
        .scheme(this.inventoryServiceProperties.getScheme())
        .host(this.inventoryServiceProperties.getHost())
        .port(this.inventoryServiceProperties.getPort())
        .path(this.inventoryServiceProperties.getProductsPath()).path("/{categoryName}")
        .build(categoryName))
      .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .body(new ParameterizedTypeReference<>() {});
    return CompletableFuture.completedFuture(response);
  }

  @Async
  public CompletableFuture<ResponsePayload<List<CategoryDTO>>> fetchCategories() {
    ResponsePayload<List<CategoryDTO>> response = this.restClient
      .get()
      .uri(uriBuilder -> uriBuilder
        .scheme(this.inventoryServiceProperties.getScheme())
        .host(this.inventoryServiceProperties.getHost())
        .port(this.inventoryServiceProperties.getPort())
        .path(this.inventoryServiceProperties.getCategoriesPath())
        .build())
      .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .body(new ParameterizedTypeReference<>() {});
    return CompletableFuture.completedFuture(response);
  }
}
