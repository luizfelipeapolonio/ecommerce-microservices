package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductPageResponseDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.external.UploadService;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ProductEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.UploadFileMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ProductRepository;
import com.felipe.response.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
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
  public ProductResponseDTO createProduct(Product product, UploadFile[] files) {
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

  @Override
  public Optional<Product> findProductById(UUID id) {
    return this.productRepository.findById(id).map(this.productEntityMapper::toDomain);
  }

  @Override
  public Product updateProduct(Product product, UpdateProductDomainDTO productDTO) {
    ProductEntity.Builder productEntityBuilder = ProductEntity.mutate(this.productEntityMapper.toEntity(product));

    if(productDTO.name() != null) {
      productEntityBuilder.name(productDTO.name());
    }
    if(productDTO.description() != null) {
      productEntityBuilder.description(productDTO.description());
    }
    if(productDTO.unitPrice() != null) {
      productEntityBuilder.unitPrice(new BigDecimal(productDTO.unitPrice()));
    }
    if(productDTO.quantity() != null) {
      productEntityBuilder.quantity(productDTO.quantity());
    }

    return this.productEntityMapper.toDomain(this.productRepository.save(productEntityBuilder.build()));
  }

  @Override
  public PageResponseDTO getProductsByCategory(String categoryName, int page, int elementsQuantity) {
    final Pageable pagination = PageRequest.of(page, elementsQuantity, Sort.by("name"));
    final Page<ProductEntity> productsPage = this.productRepository.findByCategoryName(categoryName, pagination);
    final List<Product> productsDomain = productsPage.get().map(this.productEntityMapper::toDomain).toList();

    final ResponsePayload<List<UploadService.ImageResponse>> productImages = extractIdsAndFindImages(productsPage.get());
    final List<ProductDTO> productDTOs = convertToProductDTOList(productsDomain, productImages.getPayload());

    return new ProductPageResponseDTO(productsPage, productDTOs);
  }

  @Override
  public PageResponseDTO getProductsByBrand(String brandName, int page, int elementsQuantity) {
    final Pageable pagination = PageRequest.of(page, elementsQuantity, Sort.by("name"));
    final Page<ProductEntity> productsPage = this.productRepository.findByBrandName(brandName, pagination);
    final List<Product> productsDomain = productsPage.get().map(this.productEntityMapper::toDomain).toList();

    final ResponsePayload<List<UploadService.ImageResponse>> productImages = extractIdsAndFindImages(productsPage.get());
    final List<ProductDTO> productDTOs = convertToProductDTOList(productsDomain, productImages.getPayload());

    return new ProductPageResponseDTO(productsPage, productDTOs);
  }

  @Override
  public PageResponseDTO getProductsByModel(String modelName, String brandName, int page, int elementsQuantity) {
    final Pageable pagination = PageRequest.of(page, elementsQuantity, Sort.by("name"));
    final Page<ProductEntity> productsPage = this.productRepository.findByModelNameAndBrandName(modelName, brandName, pagination);
    final List<Product> productsDomain = productsPage.get().map(this.productEntityMapper::toDomain).toList();

    final ResponsePayload<List<UploadService.ImageResponse>> productImages = extractIdsAndFindImages(productsPage.get());
    final List<ProductDTO> productDTOs = convertToProductDTOList(productsDomain, productImages.getPayload());

    return new ProductPageResponseDTO(productsPage, productDTOs);
  }

  private List<ProductDTO> convertToProductDTOList(List<Product> products, List<UploadService.ImageResponse> images) {
    final List<ProductDTO> productDTOs = new ArrayList<>(products.size());

    products.forEach(product -> {
      List<ImageFileDTO> productImages = images.stream()
        .filter(imageResponse -> imageResponse.getProductId().equals(product.getId().toString()))
        .flatMap(imageResponse -> imageResponse.getImages().stream())
        .toList();

      this.logger.info(
        "Converting product to productDTO: Id: {} - Name: {} - Images: {}",
        product.getId(), product.getName(), productImages.size()
      );

      productDTOs.add(new ProductDTO(product, productImages));
    });
    return productDTOs;
  }

  private ResponsePayload<List<UploadService.ImageResponse>> extractIdsAndFindImages(Stream<ProductEntity> products) {
    final Set<String> productIds = products
      .map(product -> product.getId().toString())
      .collect(Collectors.toSet());

    return this.uploadService.getProductImages(productIds);
  }
}
