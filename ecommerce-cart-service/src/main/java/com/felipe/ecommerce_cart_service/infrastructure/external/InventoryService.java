package com.felipe.ecommerce_cart_service.infrastructure.external;

import com.felipe.ecommerce_cart_service.infrastructure.exceptions.InventoryServiceException;
import com.felipe.response.ResponsePayload;
import com.felipe.response.product.ProductResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.UUID;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class InventoryService {

  @Value("${external.services.inventory-service.uri}")
  private String inventoryServiceUri;

  private final RestClient restClient;
  private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-cart-service";

  public InventoryService(RestClient restClient) {
    this.restClient = restClient;
  }

  public ResponsePayload<ProductResponseDTO> fetchProductById(UUID productId) {
    try {
      return this.restClient
        .get()
        .uri(URI.create(String.format("%s/%s", this.inventoryServiceUri, productId.toString())))
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });

    } catch(RestClientException ex) {
      logger.error("Error in Inventory Service RestClient -> {}", ex.getMessage());
      throw new InventoryServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }
}
