package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.CreateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.DeleteBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetAllBrandsUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.UpdateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.BrandApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.OnCreate;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.OnUpdate;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/brands")
public class BrandController implements BrandApi {
  private final CreateBrandUseCase createBrandUseCase;
  private final GetBrandByIdUseCase getBrandByIdUseCase;
  private final GetAllBrandsUseCase getAllBrandsUseCase;
  private final UpdateBrandUseCase updatedBrandUseCase;
  private final DeleteBrandUseCase deleteBrandUseCase;

  public BrandController(CreateBrandUseCase createBrandUseCase,
                         GetBrandByIdUseCase getBrandByIdUseCase,
                         GetAllBrandsUseCase getAllBrandsUseCase,
                         UpdateBrandUseCase updatedBrandUseCase,
                         DeleteBrandUseCase  deleteBrandUseCase) {
    this.createBrandUseCase = createBrandUseCase;
    this.getBrandByIdUseCase = getBrandByIdUseCase;
    this.getAllBrandsUseCase = getAllBrandsUseCase;
    this.updatedBrandUseCase = updatedBrandUseCase;
    this.deleteBrandUseCase = deleteBrandUseCase;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<BrandDTO> createBrand(@Validated(OnCreate.class) @RequestBody CreateOrUpdateBrandDTO brandDTO) {
    Brand createdBrand = this.createBrandUseCase.execute(brandDTO.name(), brandDTO.description());
    return new ResponsePayload.Builder<BrandDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Marca '" + createdBrand.getName() + "' criada com sucesso")
      .payload(new BrandDTO(createdBrand))
      .build();
  }

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<BrandDTO>> getAllBrands() {
    List<Brand> brands = this.getAllBrandsUseCase.execute();
    List<BrandDTO> brandsDTO = brands.stream().map(BrandDTO::new).toList();
    return new ResponsePayload.Builder<List<BrandDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as marcas")
      .payload(brandsDTO)
      .build();
  }

  @Override
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<BrandDTO> getBrandById(@Positive(message = "O id da marca não deve ser zero, nem valores negativos")
                                                @PathVariable Long id) {
    Brand brand = this.getBrandByIdUseCase.execute(id);
    return new ResponsePayload.Builder<BrandDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Marca de id '" + brand.getId() + "' encontrada")
      .payload(new BrandDTO(brand))
      .build();
  }

  @Override
  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<BrandDTO> updateBrand(@Positive(message = "O id da marca não deve ser zero, nem valores negativos")
                                               @PathVariable Long id,
                                               @Validated(OnUpdate.class) @RequestBody CreateOrUpdateBrandDTO brandDTO) {
    Brand updatedBrand = this.updatedBrandUseCase.execute(id, brandDTO.name(), brandDTO.description());
    return new ResponsePayload.Builder<BrandDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Marca atualizada com sucesso")
      .payload(new BrandDTO(updatedBrand))
      .build();
  }

  @Override
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<Void> deleteBrand(@Positive(message = "O id da marca não deve ser zero, nem valores negativos")
                                           @PathVariable Long id) {
    Brand deletedBrand = this.deleteBrandUseCase.execute(id);
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Marca '" + deletedBrand.getName() + "' excluída com sucesso")
      .payload(null)
      .build();
  }
}