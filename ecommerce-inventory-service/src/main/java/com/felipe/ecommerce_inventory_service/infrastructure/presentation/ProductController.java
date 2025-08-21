package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CreateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UpdateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.ProductApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.CreateProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.UpdateProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.UpdateProductResponseDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.exceptions.UnprocessableJsonException;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.UploadFileMapper;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController implements ProductApi {
  private final CreateProductUseCase createProductUseCase;
  private final UpdateProductUseCase updateProductUseCase;
  private final UploadFileMapper uploadFileMapper;
  private final ObjectMapper objectMapper;
  private final Validator validator;
  private final Logger logger = LoggerFactory.getLogger(ProductController.class);
  
  public ProductController(CreateProductUseCase createProductUseCase,
                           UpdateProductUseCase updateProductUseCase,
                           UploadFileMapper uploadFileMapper,
                           ObjectMapper objectMapper,
                           Validator validator) {
    this.createProductUseCase = createProductUseCase;
    this.updateProductUseCase = updateProductUseCase;
    this.uploadFileMapper = uploadFileMapper;
    this.objectMapper = objectMapper;
    this.validator = validator;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CreateProductResponseDTO> createProduct(@RequestPart("productDTO") String jsonProductDTO,
                                                                 @RequestPart("images") MultipartFile[] images) {
    CreateProductDTO productDTO = convertJsonToCreateProductDTO(jsonProductDTO);
    UploadFile[] files = Stream.of(images)
      .map(this.uploadFileMapper::toUploadFile)
      .toArray(UploadFile[]::new);

    CreateProductResponseDTO product = this.createProductUseCase.execute(productDTO, files);

    return new ResponsePayload.Builder<CreateProductResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Produto '" + product.name() + "' inserido com sucesso")
      .payload(product)
      .build();
  }

  @Override
  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<UpdateProductResponseDTO> updateProduct(@PathVariable UUID id,
                                                                 @Valid @RequestBody UpdateProductDTO productDTO) {
    Product updatedProduct = this.updateProductUseCase.execute(id, productDTO);
    return new ResponsePayload.Builder<UpdateProductResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Produto atualizado com sucesso")
      .payload(new UpdateProductResponseDTO(updatedProduct))
      .build();
  }

  private CreateProductDTO convertJsonToCreateProductDTO(String json) {
    try {
      CreateProductDTO convertedObject = this.objectMapper.readValue(json, CreateProductDTO.class);
      Set<ConstraintViolation<CreateProductDTO>> violations = this.validator.validate(convertedObject);
      if(!violations.isEmpty()) {
        throw new ConstraintViolationException(violations);
      }
      return convertedObject;

    } catch(JsonProcessingException ex) {
      this.logger.error("Error on convert JSON 'productDTO' to CreateProductDTO: {}", ex.getMessage(), ex);
      throw new UnprocessableJsonException("Não foi possível converter JSON em objeto", ex);
    }
  }
}
