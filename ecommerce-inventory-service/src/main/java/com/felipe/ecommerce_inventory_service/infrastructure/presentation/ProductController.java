package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CreateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetAllProductsUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsUseCase;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  private final GetProductsUseCase getProductsUseCase;
  private final GetAllProductsUseCase getAllProductsUseCase;
  private final GetProductsByCategoryUseCase getProductsByCategoryUseCase;
  private final GetProductsByBrandUseCase getProductsByBrandUseCase;
  private final GetProductsByModelUseCase getProductsByModelUseCase;
  private final UploadFileMapper uploadFileMapper;
  private final ObjectMapper objectMapper;
  private final Validator validator;
  private final Logger logger = LoggerFactory.getLogger(ProductController.class);
  
  public ProductController(CreateProductUseCase createProductUseCase,
                           UpdateProductUseCase updateProductUseCase,
                           GetProductsUseCase getProductsUseCase,
                           GetAllProductsUseCase getAllProductsUseCase,
                           GetProductsByCategoryUseCase getProductsByCategoryUseCase,
                           GetProductsByBrandUseCase getProductsByBrandUseCase,
                           GetProductsByModelUseCase getProductsByModelUseCase,
                           UploadFileMapper uploadFileMapper,
                           ObjectMapper objectMapper,
                           Validator validator) {
    this.createProductUseCase = createProductUseCase;
    this.updateProductUseCase = updateProductUseCase;
    this.getProductsUseCase = getProductsUseCase;
    this.getAllProductsUseCase = getAllProductsUseCase;
    this.getProductsByCategoryUseCase = getProductsByCategoryUseCase;
    this.getProductsByBrandUseCase = getProductsByBrandUseCase;
    this.getProductsByModelUseCase = getProductsByModelUseCase;
    this.uploadFileMapper = uploadFileMapper;
    this.objectMapper = objectMapper;
    this.validator = validator;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<ProductResponseDTO> createProduct(@RequestPart("productDTO") String jsonProductDTO,
                                                           @RequestPart("images") MultipartFile[] images) {
    CreateProductDTO productDTO = convertJsonToCreateProductDTO(jsonProductDTO);
    UploadFile[] files = Stream.of(images)
      .map(this.uploadFileMapper::toUploadFile)
      .toArray(UploadFile[]::new);

    ProductResponseDTO product = this.createProductUseCase.execute(productDTO, files);

    return new ResponsePayload.Builder<ProductResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Produto '" + product.name() + "' inserido com sucesso")
      .payload(product)
      .build();
  }

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PageResponseDTO> getProducts(@RequestParam(name = "category", required = false) String categoryName,
                                                      @RequestParam(name = "brand", required = false) String brandName,
                                                      @RequestParam(name = "model", required = false) String modelName,
                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                      @RequestParam(name = "pageSize", defaultValue = "10") int size) {
    PageResponseDTO products = this.getProductsUseCase.execute(categoryName, brandName, modelName, page, size);
    final String message = String.format(
      "Produtos da categoria: '%s' - marca: '%s' - modelo: '%s'",
      categoryName == null ? "N/A" : categoryName,
      brandName == null ? "N/A" : brandName,
      modelName == null ? "N/A" : modelName
    );
    return new ResponsePayload.Builder<PageResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message(message)
      .payload(products)
      .build();
  }

  @Override
  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PageResponseDTO> getAllProducts(@RequestParam(name = "page", defaultValue = "0") int page,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") int size) {
    PageResponseDTO products = this.getAllProductsUseCase.execute(page, size);
    return new ResponsePayload.Builder<PageResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os produtos - página: " + page + " - quantidade de produtos: " + products.currentElements())
      .payload(products)
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

  @Override
  @GetMapping("/category/{categoryName}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PageResponseDTO> getProductsByCategory(@PathVariable String categoryName,
                                                                @RequestParam(name = "page", defaultValue = "0") int page,
                                                                @RequestParam(name = "pageSize", defaultValue = "10") int size) {
    PageResponseDTO productsPage = this.getProductsByCategoryUseCase.execute(categoryName, page, size);
    return new ResponsePayload.Builder<PageResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Produtos da categoria '" + categoryName + "'")
      .payload(productsPage)
      .build();
  }

  @Override
  @GetMapping("/brand/{brandName}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PageResponseDTO> getProductsByBrand(@PathVariable String brandName,
                                                             @RequestParam(name = "page") int page,
                                                             @RequestParam(name = "pageSize") int size) {
    PageResponseDTO products = this.getProductsByBrandUseCase.execute(brandName, page, size);
    return new ResponsePayload.Builder<PageResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Produtos da marca '" + brandName + "'")
      .payload(products)
      .build();
  }

  @Override
  @GetMapping("/model/{modelName}/{brandName}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PageResponseDTO> getProductsByModel(@PathVariable String modelName,
                                                             @PathVariable String brandName,
                                                             @RequestParam(name = "page") int page,
                                                             @RequestParam(name = "pageSize") int size) {
    PageResponseDTO products = this.getProductsByModelUseCase.execute(modelName, brandName, page, size);
    return new ResponsePayload.Builder<PageResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Produtos do modelo '" + modelName + "' da marca '" + brandName + "'")
      .payload(products)
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
