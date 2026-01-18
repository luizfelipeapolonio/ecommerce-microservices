package com.felipe.ecommerce_customer_service.infrastructure.gateway;

import com.felipe.ecommerce_customer_service.core.application.exceptions.AuthServerException;
import com.felipe.ecommerce_customer_service.core.application.gateway.AuthServerGateway;
import com.felipe.ecommerce_customer_service.core.application.dtos.CustomerAuthDataDTO;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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

  @Value("${external.services.auth-server.internal-uri}")
  private String authServerUri;

  public AuthServerGatewayImpl(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  @CircuitBreaker(name = "createCustomerCircuitBreaker", fallbackMethod = "fallback")
  @RateLimiter(name = "createCustomerCircuitBreaker", fallbackMethod = "fallback")
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

  private void fallback(CallNotPermittedException ex) {
    this.logger.error("Circuitbreaker fallback -> {}", ex.getMessage());
    throw ex;
  }

  private void fallback(RequestNotPermitted ex) {
    this.logger.error("RateLimiter fallback -> {}", ex.getMessage());
    throw ex;
  }
}
