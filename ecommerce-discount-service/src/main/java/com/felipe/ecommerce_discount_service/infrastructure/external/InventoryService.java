package com.felipe.ecommerce_discount_service.infrastructure.external;

import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.response.ResponsePayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

  public InventoryService(RestClient restClient) {
    this.restClient = restClient;
  }

  public ResponsePayload<Map<String, Integer>> applyPromotion(PromotionRequest promotionRequest) {
    return this.restClient
      .post()
      .uri(URI.create(this.inventoryServiceUrl))
      .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
      .contentType(MediaType.APPLICATION_JSON)
      .body(promotionRequest)
      .retrieve()
      .body(new ParameterizedTypeReference<>() {});
  }

  public static class PromotionRequest {
    private final String promotionScope;
    private final String discountType;
    private final String discountValue;
    private final String endDate;
    private final String minimumPrice;
    private final List<PromotionAppliesToDTOImpl> targets;

    public PromotionRequest(PromotionEntity promotionEntity, List<PromotionAppliesToDTOImpl> targets) {
      this.promotionScope = promotionEntity.getScope();
      this.discountType = promotionEntity.getDiscountType();
      this.discountValue = promotionEntity.getDiscountValue();
      this.endDate = promotionEntity.getEndDate().toString();
      this.minimumPrice = promotionEntity.getMinimumPrice().toString();
      this.targets = targets;
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
