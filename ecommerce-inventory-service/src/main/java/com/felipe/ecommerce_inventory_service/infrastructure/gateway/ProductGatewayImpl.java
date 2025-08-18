package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.external.UploadService;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ProductEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.UploadFileMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ProductRepository;
import com.felipe.response.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class ProductGatewayImpl implements ProductGateway {
  private final ProductRepository productRepository;
  private final UploadService uploadService;
  private final ProductEntityMapper productEntityMapper;
  private final UploadFileMapper uploadFileMapper;
  private final Logger logger = LoggerFactory.getLogger(ProductGatewayImpl.class);

  public ProductGatewayImpl(ProductRepository productRepository,
                            UploadService uploadService,
                            ProductEntityMapper productEntityMapper,
                            UploadFileMapper uploadFileMapper) {
    this.productRepository = productRepository;
    this.uploadService = uploadService;
    this.productEntityMapper = productEntityMapper;
    this.uploadFileMapper = uploadFileMapper;
  }

  @Override
  public CreateProductResponseDTO createProduct(Product product, UploadFile[] files) {
    ProductEntity productEntity = this.productEntityMapper.toEntity(product);
    MultipartFile[] images = Stream.of(files)
      .map(this.uploadFileMapper::toMultipartFile)
      .toArray(MultipartFile[]::new);

    ProductEntity createdProduct = this.productRepository.save(productEntity);
    this.logger.info("Created product - Id: {} - Name: {}", createdProduct.getId(), createdProduct.getName());

    UploadService.ProductData productData = new UploadService.ProductData(
      createdProduct.getName(),
      createdProduct.getId().toString()
    );
    ResponsePayload<List<ImageFileDTO>> uploadedImages = this.uploadService.upload(productData, images);
    this.logger.info(
      "Uploaded images - Response: {} - StatusCode: {} - ImageQuantity: {}",
      uploadedImages.getType(), uploadedImages.getCode(), uploadedImages.getPayload().size()
    );

    return new ProductDTO(this.productEntityMapper.toDomain(createdProduct), uploadedImages.getPayload());
  }

  @Override
  public Optional<Product> findProductByName(String name) {
    return this.productRepository.findByName(name).map(this.productEntityMapper::toDomain);
  }
}
