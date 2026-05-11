package com.felipe.ecommerce_mail_service.external;

import com.felipe.ecommerce_mail_service.exceptions.OrderServiceException;
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
import java.util.List;
import java.util.UUID;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class OrderService {

  @Value("${services.order-service.url}")
  private String orderServiceUrl;
  private final RestClient restClient;
  private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-mail-service";

  public OrderService(RestClient restClient) {
    this.restClient = restClient;
  }

  public OrderResponseDTO fetchOrderInfo(UUID orderId) {
    try {
      ResponsePayload<OrderResponseDTO> response = this.restClient
        .get()
        .uri(URI.create(this.orderServiceUrl + "/" + orderId))
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });

      assert response != null;
      return response.getPayload();
    } catch (RestClientException ex) {
      logger.error("Error in Order Service RestClient -> {}", ex.getMessage(), ex);
      throw new OrderServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }

  public record OrderResponseDTO(String id,
                                 String orderPrice,
                                 boolean withCoupon,
                                 String couponCode,
                                 String couponValue,
                                 String shippingFee,
                                 String checkoutUrl,
                                 String invoiceUrl,
                                 String customerId,
                                 String status,
                                 String createdAt,
                                 String updatedAt,
                                 List<Item> orderItems) {}

  public record Item(long id, String productId, String productName, long quantity, String finalPrice, String addedAt) {}
}
