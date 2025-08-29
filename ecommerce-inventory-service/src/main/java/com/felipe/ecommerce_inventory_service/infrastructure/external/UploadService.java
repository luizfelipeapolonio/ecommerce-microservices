package com.felipe.ecommerce_inventory_service.infrastructure.external;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.response.ResponsePayload;
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
  private final Logger logger = LoggerFactory.getLogger(UploadService.class);

  public UploadService(RestClient restClient) {
    this.restClient = restClient;
  }

  public ResponsePayload<List<ImageFileDTO>> upload(ProductData productData, MultipartFile[] images) {
    MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
    parts.add("productData", productData);

    for(MultipartFile image : images) {
      parts.add("images", image.getResource());
    }

    return this.restClient
      .post()
      .uri(URI.create(this.uploadServiceUrl))
      .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .body(parts)
      .retrieve()
      .body(new ParameterizedTypeReference<>() {});
  }

  public ResponsePayload<List<ImageResponse>> getProductImages(Set<String> productIdsList)  {
    final String productIds = StringUtils.collectionToCommaDelimitedString(productIdsList);
    this.logger.info("Get product images for productIds: {}", productIds);

    return this.restClient
      .get()
      .uri(URI.create(this.uploadServiceUrl))
      .header("productIds", productIds)
      .attributes(clientRegistrationId(CLIENT_REGISTRATION_ID))
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .body(new ParameterizedTypeReference<>() {});
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
}
