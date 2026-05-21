package com.felipe.ecommerce_catalog_service.presentation;

import com.felipe.ecommerce_catalog_service.dtos.HomepageProductsDTO;
import com.felipe.ecommerce_catalog_service.services.HomepageService;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {
  private final HomepageService homepageService;

  public CatalogController(HomepageService homepageService) {
    this.homepageService = homepageService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<HomepageProductsDTO> getHomepageProducts() {
    HomepageProductsDTO products = this.homepageService.getHomepageProducts();
    return new ResponsePayload.Builder<HomepageProductsDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Catálogo")
      .payload(products)
      .build();
  }
}
