package com.felipe.ecommerce_order_service.infrastructure.external;

import com.felipe.ecommerce_order_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.CouponServiceException;
import com.felipe.ecommerce_order_service.infrastructure.exceptions.InvalidCouponException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.net.URI;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class CouponService implements CouponGateway {

  @Value("${services.discount-service.url}")
  private String discountServiceUrl;
  private final RestClient restClient;
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-order-service";
  private static final Logger logger = LoggerFactory.getLogger(CouponService.class);

  public CouponService(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void checkIfCouponIsValid(String couponCode) {
    try {
      String response = this.restClient
        .get()
        .uri(URI.create(this.discountServiceUrl) + "/check?couponCode={code}", couponCode)
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(String.class);
      logger.info("CouponService RestClient - checkIfCouponIsValid response: {}", response);
    } catch (RestClientException ex) {
      logger.error("Error in CouponService RestClient - checkIfCouponIsValid -> message: {}", ex.getMessage(), ex);
      if (ex instanceof HttpClientErrorException.UnprocessableEntity ||
          ex instanceof HttpClientErrorException.NotFound) {
        throw new InvalidCouponException("Cupom inválido");
      }
      throw new CouponServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }
}
