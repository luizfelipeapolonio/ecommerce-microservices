package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.CreateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.BrandApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/brands")
public class BrandController implements BrandApi {
  private final CreateBrandUseCase createBrandUseCase;

  public BrandController(CreateBrandUseCase createBrandUseCase) {
    this.createBrandUseCase = createBrandUseCase;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<BrandDTO> createBrand(@Valid @RequestBody CreateOrUpdateBrandDTO brandDTO) {
    Brand createdBrand = this.createBrandUseCase.execute(brandDTO.name(), brandDTO.description());
    return new ResponsePayload.Builder<BrandDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Marca '" + createdBrand.getName() + "' criada com sucesso")
      .payload(new BrandDTO(createdBrand))
      .build();
  }
}
