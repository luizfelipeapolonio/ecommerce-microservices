package com.felipe.ecommerce_inventory_service.infrastructure.external;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.UploadServiceException;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.List;
import java.util.Set;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Service
public class UploadService {
  private final RestClient restClient;

  @Value("${uploadService.url}")
  private String uploadServiceUrl;
  private static final String CLIENT_REGISTRATION_ID = "ecommerce-inventory-service";
  private static final Logger logger = LoggerFactory.getLogger(UploadService.class);

  public UploadService(RestClient restClient) {
    this.restClient = restClient;
  }

  @CircuitBreaker(name = "inventory__uploadService", fallbackMethod = "fallback")
  @RateLimiter(name = "inventory__uploadService", fallbackMethod = "fallback")
  public ResponsePayload<List<ImageFileDTO>> upload(ProductData productData, MultipartFile[] images) {
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("productData", productData);

    for(MultipartFile image : images) {
      parts.add("images", image.getResource());
    }

    try {
      return this.restClient
        .post()
        .uri(URI.create(this.uploadServiceUrl))
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .contentType(MediaType.MULTIPART_FORM_DATA)
        .body(parts)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });
    } catch(RestClientException ex) {
      logger.error("RestClient error in upload(): {}", ex.getMessage());
      throw new UploadServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }

  @CircuitBreaker(name = "inventory__uploadService", fallbackMethod = "fallback")
  @RateLimiter(name = "inventory__uploadService", fallbackMethod = "fallback")
  public ResponsePayload<List<ImageResponse>> getProductImages(Set<String> productIdsList)  {
    final String productIds = StringUtils.collectionToCommaDelimitedString(productIdsList);
    logger.info("Get product images for productIds: {}", productIds);

    try {
      return this.restClient
        .get()
        .uri(URI.create(this.uploadServiceUrl))
        .header("productIds", productIds)
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });
    } catch(RestClientException ex) {
      logger.error("RestClient error in getProductImages(): {}", ex.getMessage());
      throw new UploadServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }

  @CircuitBreaker(name = "inventory__uploadService", fallbackMethod = "fallback")
  @RateLimiter(name = "inventory__uploadService", fallbackMethod = "fallback")
  public ResponsePayload<DeleteImagesResponse> deleteImages(String productId) {
    try {
      return this.restClient
        .delete()
        .uri(URI.create(this.uploadServiceUrl))
        .header("productId", productId)
        .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .body(new ParameterizedTypeReference<>() {
        });
    } catch(RestClientException ex) {
      logger.error("RestClient error in deleteImages(): {}", ex.getMessage());
      throw new UploadServiceException("Ocorreu um erro ao se comunicar com a aplicação");
    }
  }

  private ResponsePayload<List<ImageFileDTO>> fallback(ProductData productData, MultipartFile[] images, CallNotPermittedException ex) {
    logger.error("upload() CircuitBreaker fallback -> {}", ex.getMessage());
    throw ex;
  }

  private ResponsePayload<List<ImageFileDTO>> fallback(ProductData productData, MultipartFile[] images, RequestNotPermitted ex) {
    logger.error("upload() RateLimiter fallback -> {}", ex.getMessage());
    throw ex;
  }

  private ResponsePayload<List<ImageResponse>> fallback(Set<String> productIdsList, CallNotPermittedException ex) {
    logger.error("getProductImages() CircuitBreaker fallback -> {}", ex.getMessage());
    throw ex;
  }

  private ResponsePayload<List<ImageResponse>> fallback(Set<String> productIdsList, RequestNotPermitted ex) {
    logger.error("getProductImages() RateLimiter fallback -> {}", ex.getMessage());
    throw ex;
  }

  private ResponsePayload<DeleteImagesResponse> fallback(String productId, CallNotPermittedException ex) {
    logger.error("deleteImages() CircuitBreaker fallback -> {}", ex.getMessage());
    throw ex;
  }

  private ResponsePayload<DeleteImagesResponse> fallback(String productId, RequestNotPermitted ex) {
    logger.error("deleteImages() RateLimiter fallback -> {}", ex.getMessage());
    throw ex;
  }

  public static class ProductData {
    private final String productName;
    private final String productId;

    public ProductData(String productName, String productId) {
      this.productName = productName;
      this.productId = productId;
    }

    public String getProductName() {
      return this.productName;
    }

    public String getProductId() {
      return this.productId;
    }
  }

  public static class ImageResponse {
    private final String productId;
    private final List<ImageFileDTO> images;

    public ImageResponse(String productId, List<ImageFileDTO> images) {
      this.productId = productId;
      this.images = images;
    }

    public String getProductId() {
      return this.productId;
    }

    public List<ImageFileDTO> getImages() {
      return this.images;
    }
  }

  public static class DeleteImagesResponse {
    private final String productId;
    private final int deletedImagesQuantity;

    public DeleteImagesResponse(String productId, int deletedImagesQuantity) {
      this.productId = productId;
      this.deletedImagesQuantity = deletedImagesQuantity;
    }

    public String getProductId() {
      return this.productId;
    }

    public int getDeletedImagesQuantity() {
      return this.deletedImagesQuantity;
    }
  }
}
