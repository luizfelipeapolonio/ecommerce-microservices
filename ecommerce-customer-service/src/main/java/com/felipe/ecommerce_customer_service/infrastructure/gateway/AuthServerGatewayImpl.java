package com.felipe.ecommerce_customer_service.infrastructure.gateway;

import com.felipe.ecommerce_customer_service.core.application.exceptions.AuthServerException;
import com.felipe.ecommerce_customer_service.core.application.gateway.AuthServerGateway;
import com.felipe.ecommerce_customer_service.core.application.dtos.CustomerAuthDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Component
public class AuthServerGatewayImpl implements AuthServerGateway {
  private final RestClient restClient;
  private final Logger logger = LoggerFactory.getLogger(AuthServerGatewayImpl.class);

  @Value("${auth.server.internal-uri}")
  private String authServerUri;

  public AuthServerGatewayImpl(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void registerCustomer(CustomerAuthDataDTO customerData) throws AuthServerException {
    try {
      String body = this.restClient
        .post()
        .uri(this.authServerUri)
        .attributes(clientRegistrationId("ecommerce-customer-service"))
        .body(customerData)
        .retrieve()
        .body(String.class);

      this.logger.info("Auth Server rest client response -> {}", body);
    } catch(RestClientException ex) {
      this.logger.error("Auth Server rest client call -> {}", ex.getMessage());
      throw new AuthServerException("Ocorreu um erro ao se comunicar com o servidor", ex);
    }
  }
}
