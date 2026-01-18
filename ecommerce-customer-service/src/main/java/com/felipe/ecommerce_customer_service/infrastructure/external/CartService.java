package com.felipe.ecommerce_customer_service.infrastructure.external;

import com.felipe.ecommerce_customer_service.infrastructure.exceptions.CartServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;
import java.util.UUID;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class CartService {
  private final RestClient restClient;

  @Value("${external.services.cart-service.uri}")
  private String cartServiceUri;
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-customer-service";
  private final Logger logger = LoggerFactory.getLogger(CartService.class);

  public CartService(RestClient restClient) {
    this.restClient = restClient;
  }

  public void createCart(CreateCartDTO createCartDTO) {
    try {
      final String response = this.restClient
        .post()
        .uri(URI.create(this.cartServiceUri))
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .contentType(MediaType.APPLICATION_JSON)
        .body(createCartDTO)
        .retrieve()
        .body(String.class);

      this.logger.info("Create Cart response: {}", response);
    } catch(RestClientException ex) {
      this.logger.error("RestClient error in createCart: {}", ex.getMessage());
      throw new CartServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }

  public static class CreateCartDTO {
    private final UUID customerId;

    public CreateCartDTO(UUID customerId) {
      this.customerId = customerId;
    }

    public UUID getCustomerId() {
      return this.customerId;
    }
  }
}
