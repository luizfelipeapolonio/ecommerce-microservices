package com.felipe.ecommerce_discount_service.infrastructure.external;

import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.InventoryServiceException;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.response.ResponsePayload;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class InventoryService {
  private final RestClient restClient;

  @Value("${services.inventory-service.url}")
  private String inventoryServiceUrl;
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-discount-service";
  private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);

  public InventoryService(RestClient restClient) {
    this.restClient = restClient;
  }

  @CircuitBreaker(name = "discount__inventoryService", fallbackMethod = "fallback")
  @RateLimiter(name = "discount__inventoryService", fallbackMethod = "fallback")
  public ResponsePayload<Map<String, Integer>> applyPromotion(PromotionRequest promotionRequest) {
    try {
      return this.restClient
        .post()
        .uri(URI.create(this.inventoryServiceUrl))
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .contentType(MediaType.APPLICATION_JSON)
        .body(promotionRequest)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });
    } catch(RestClientException ex) {
      logger.error("RestClient error in applyPromotion: {}", ex.getMessage());
      throw new InventoryServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }

  private ResponsePayload<Map<String, Integer>> fallback(PromotionRequest promotionRequest, CallNotPermittedException ex) {
    logger.error("Inventory Service Circuit Breaker fallback -> {}", ex.getMessage());
    throw ex;
  }

  private ResponsePayload<Map<String, Integer>> fallback(PromotionRequest promotionRequest, RequestNotPermitted ex) {
    logger.error("Inventory Service RateLimiter fallback -> {}", ex.getMessage());
    throw ex;
  }

  public static class PromotionRequest {
    private final String promotionId;
    private final String promotionScope;
    private final String discountType;
    private final String discountValue;
    private final String endDate;
    private final String minimumPrice;
    private final List<PromotionAppliesToDTOImpl> targets;

    public PromotionRequest(PromotionEntity promotionEntity, List<PromotionAppliesToDTOImpl> targets) {
      this.promotionId = promotionEntity.getId().toString();
      this.promotionScope = promotionEntity.getScope();
      this.discountType = promotionEntity.getDiscountType();
      this.discountValue = promotionEntity.getDiscountValue();
      this.endDate = promotionEntity.getEndDate().toString();
      this.minimumPrice = promotionEntity.getMinimumPrice().toString();
      this.targets = targets;
    }

    public String getPromotionId() {
      return this.promotionId;
    }

    public String getPromotionScope() {
      return this.promotionScope;
    }

    public String getDiscountType() {
      return this.discountType;
    }

    public String getDiscountValue() {
      return this.discountValue;
    }

    public String getEndDate() {
      return this.endDate;
    }

    public String getMinimumPrice() {
      return this.minimumPrice;
    }

    public List<PromotionAppliesToDTOImpl> getTargets() {
      return this.targets;
    }
  }
}
