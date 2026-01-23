package com.felipe.ecommerce_cart_service.infrastructure.external;

import com.felipe.ecommerce_cart_service.core.application.dtos.CustomerProfileDTO;
import com.felipe.ecommerce_cart_service.core.application.gateway.CustomerGateway;
import com.felipe.ecommerce_cart_service.infrastructure.exceptions.CustomerServiceException;
import com.felipe.response.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class CustomerService implements CustomerGateway {

  @Value("${external.services.customer-service.uri}")
  private String customerServiceUri;

  private final RestClient restClient;
  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-cart-service";

  public CustomerService(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public CustomerProfileDTO fetchAuthCustomerProfile(String customerEmail) {
    try {
      final ResponsePayload<CustomerProfileDTO> response = this.restClient
        .get()
        .uri(URI.create(this.customerServiceUri + "/profile"))
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .header("authCustomerEmail", customerEmail)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {});

      assert response != null;
      return response.getPayload();
    } catch(RestClientException ex) {
      logger.error("Error in Customer Service RestClient -> {}", ex.getMessage());
      throw new CustomerServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }
}
