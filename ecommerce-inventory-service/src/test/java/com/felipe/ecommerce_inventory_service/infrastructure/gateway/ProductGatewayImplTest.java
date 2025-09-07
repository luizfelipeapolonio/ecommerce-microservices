package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PageResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UploadFileImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.UpdateProductDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.external.UploadService;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ProductEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.UploadFileMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ProductRepository;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
public class ProductGatewayImplTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private UploadService uploadService;

  @Mock
  private ProductEntityMapper productEntityMapper;

  @Mock
  private UploadFileMapper uploadFileMapper;

  @InjectMocks
  private ProductGatewayImpl productGateway;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createProductSuccess - Should successfully create a product and return it")
  void createProductSuccess() {
    Product product = this.dataMock.getProductsDomain().getFirst();
    ProductEntity productEntity = this.dataMock.getProductsEntity().getFirst();
    UploadFile[] files = {
      UploadFileImpl.builder().originalFileName("image1").build(),
      UploadFileImpl.builder().originalFileName("image2").build()
    };
    byte[] content = {10, 20, 30};
    MultipartFile[] images = {
      new MockMultipartFile("image1", content),
      new MockMultipartFile("image2", content)
    };
    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    ArgumentCaptor<UploadService.ProductData> productDataCaptor = ArgumentCaptor.forClass(UploadService.ProductData.class);

    ResponsePayload<List<ImageFileDTO>> uploadResponse = new ResponsePayload.Builder<List<ImageFileDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Imagens salvas com sucesso")
      .payload(List.of(image1))
      .build();

    when(this.productEntityMapper.toEntity(product)).thenReturn(productEntity);
    when(this.uploadFileMapper.toMultipartFile(files[0])).thenReturn(images[0]);
    when(this.uploadFileMapper.toMultipartFile(files[1])).thenReturn(images[1]);
    when(this.productRepository.save(productEntity)).thenReturn(productEntity);
    when(this.uploadService.upload(productDataCaptor.capture(), eq(images))).thenReturn(uploadResponse);
    when(this.productEntityMapper.toDomain(productEntity)).thenReturn(product);

    ProductResponseDTO createdProduct = this.productGateway.createProduct(product, files);

    // Argument captor assertions
    assertThat(productDataCaptor.getValue().getProductId()).isEqualTo(productEntity.getId().toString());
    assertThat(productDataCaptor.getValue().getProductName()).isEqualTo(productEntity.getName());
    // created product assertions
    assertThat(createdProduct.id()).isEqualTo(product.getId().toString());
    assertThat(createdProduct.name()).isEqualTo(product.getName());
    assertThat(createdProduct.description()).isEqualTo(product.getDescription());
    assertThat(createdProduct.unitPrice()).isEqualTo(product.getUnitPrice().toString());
    assertThat(createdProduct.quantity()).isEqualTo(product.getQuantity());
    assertThat(createdProduct.createdAt()).isEqualTo(product.getCreatedAt().toString());
    assertThat(createdProduct.updatedAt()).isEqualTo(product.getUpdatedAt().toString());
    assertThat(createdProduct.images().size()).isEqualTo(uploadResponse.getPayload().size());

    verify(this.productEntityMapper, times(1)).toEntity(product);
    verify(this.uploadFileMapper, times(1)).toMultipartFile(files[0]);
    verify(this.uploadFileMapper, times(1)).toMultipartFile(files[1]);
    verify(this.productRepository, times(1)).save(productEntity);
    verify(this.uploadService, times(1)).upload(any(UploadService.ProductData.class), eq(images));
    verify(this.productEntityMapper, times(1)).toDomain(productEntity);
  }

  @Test
  @DisplayName("findProductByNameReturnsOptionalOfProduct - Should successfully find a product with the given name and return an Optional of Product")
  void findProductByNameReturnsOptionalOfProduct() {
    ProductEntity productEntity = this.dataMock.getProductsEntity().getFirst();
    Product product = this.dataMock.getProductsDomain().getFirst();

    when(this.productRepository.findByName(productEntity.getName())).thenReturn(Optional.of(productEntity));
    when(this.productEntityMapper.toDomain(productEntity)).thenReturn(product);

    Optional<Product> foundProduct = this.productGateway.findProductByName(productEntity.getName());

    assertThat(foundProduct.isPresent()).isTrue();
    assertThat(foundProduct.get().getId()).isEqualTo(product.getId());
    assertThat(foundProduct.get().getName()).isEqualTo(product.getName());
    assertThat(foundProduct.get().getDescription()).isEqualTo(product.getDescription());
    assertThat(foundProduct.get().getQuantity()).isEqualTo(product.getQuantity());
    assertThat(foundProduct.get().getUnitPrice()).isEqualTo(product.getUnitPrice());
    assertThat(foundProduct.get().getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(foundProduct.get().getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(foundProduct.get().getCategory().getId()).isEqualTo(product.getCategory().getId());
    assertThat(foundProduct.get().getBrand().getId()).isEqualTo(product.getBrand().getId());
    assertThat(foundProduct.get().getModel().getId()).isEqualTo(product.getModel().getId());

    verify(this.productRepository, times(1)).findByName(productEntity.getName());
    verify(this.productEntityMapper, times(1)).toDomain(productEntity);
  }

  @Test
  @DisplayName("findProductByIdReturnsOptionalOfProduct - Should successfully find a product with the given id and return an Optional of Product")
  void findProductByIdReturnsOptionalOfProduct() {
    ProductEntity productEntity = this.dataMock.getProductsEntity().getFirst();
    Product product = this.dataMock.getProductsDomain().getFirst();

    when(this.productRepository.findById(productEntity.getId())).thenReturn(Optional.of(productEntity));
    when(this.productEntityMapper.toDomain(productEntity)).thenReturn(product);

    Optional<Product> foundProduct = this.productGateway.findProductById(productEntity.getId());

    assertThat(foundProduct.isPresent()).isTrue();
    assertThat(foundProduct.get().getId()).isEqualTo(product.getId());
    assertThat(foundProduct.get().getName()).isEqualTo(product.getName());
    assertThat(foundProduct.get().getDescription()).isEqualTo(product.getDescription());
    assertThat(foundProduct.get().getQuantity()).isEqualTo(product.getQuantity());
    assertThat(foundProduct.get().getUnitPrice()).isEqualTo(product.getUnitPrice());
    assertThat(foundProduct.get().getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(foundProduct.get().getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(foundProduct.get().getCategory()).usingRecursiveComparison().isEqualTo(product.getCategory());
    assertThat(foundProduct.get().getBrand()).usingRecursiveComparison().isEqualTo(product.getBrand());
    assertThat(foundProduct.get().getModel()).usingRecursiveComparison().isEqualTo(product.getModel());

    verify(this.productRepository, times(1)).findById(productEntity.getId());
    verify(this.productEntityMapper, times(1)).toDomain(productEntity);
  }

  @Test
  @DisplayName("updateProductSuccess - Should successfully update a product and return it")
  void updateProductSuccess() {
    Product product = this.dataMock.getProductsDomain().getFirst();
    ProductEntity productEntity = this.dataMock.getProductsEntity().getFirst();
    UpdateProductDomainDTO productDTO = new UpdateProductDTO(
      "Updated name",
      "Updated description",
      "100.00",
      10L
    );
    ArgumentCaptor<ProductEntity> entityCaptor = ArgumentCaptor.forClass(ProductEntity.class);

    when(this.productEntityMapper.toEntity(product)).thenReturn(productEntity);
    when(this.productRepository.save(entityCaptor.capture())).thenReturn(productEntity);
    when(this.productEntityMapper.toDomain(productEntity)).thenReturn(product);

    Product updatedProduct = this.productGateway.updateProduct(product, productDTO);

    // Argument captor assertion
    assertThat(entityCaptor.getValue().getName()).isEqualTo(productDTO.name());
    assertThat(entityCaptor.getValue().getDescription()).isEqualTo(productDTO.description());
    assertThat(entityCaptor.getValue().getUnitPrice().toString()).isEqualTo(productDTO.unitPrice());
    assertThat(entityCaptor.getValue().getQuantity()).isEqualTo(productDTO.quantity());
    // Updated product assertion
    assertThat(updatedProduct.getId()).isEqualTo(product.getId());
    assertThat(updatedProduct.getName()).isEqualTo(product.getName());
    assertThat(updatedProduct.getDescription()).isEqualTo(product.getDescription());
    assertThat(updatedProduct.getUnitPrice().toString()).isEqualTo(product.getUnitPrice().toString());
    assertThat(updatedProduct.getQuantity()).isEqualTo(product.getQuantity());
    assertThat(updatedProduct.getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(updatedProduct.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(updatedProduct.getCategory()).usingRecursiveComparison().isEqualTo(product.getCategory());
    assertThat(updatedProduct.getBrand()).usingRecursiveComparison().isEqualTo(product.getBrand());
    assertThat(updatedProduct.getModel()).usingRecursiveComparison().isEqualTo(product.getModel());

    verify(this.productEntityMapper, times(1)).toEntity(product);
    verify(this.productRepository, times(1)).save(any(ProductEntity.class));
    verify(this.productEntityMapper, times(1)).toDomain(productEntity);
  }

  @Test
  @DisplayName("getProductSuccess - Should successfully find and return a product")
  void getProductSuccess() {
    final Product productDomain = this.dataMock.getProductsDomain().getFirst();
    final Set<String> productId = Set.of(productDomain.getId().toString());
    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    final UploadService.ImageResponse productImage = new UploadService.ImageResponse(
      productDomain.getId().toString(),
      List.of(image1)
    );
    final var uploadResponse = new ResponsePayload.Builder<List<UploadService.ImageResponse>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Imagens dos produtos")
      .payload(List.of(productImage))
      .build();

    when(this.uploadService.getProductImages(productId)).thenReturn(uploadResponse);

    ProductResponseDTO product = this.productGateway.getProduct(productDomain);

    assertThat(product.id()).isEqualTo(productDomain.getId().toString());
    assertThat(product.name()).isEqualTo(productDomain.getName());
    assertThat(product.description()).isEqualTo(productDomain.getDescription());
    assertThat(product.quantity()).isEqualTo(productDomain.getQuantity());
    assertThat(product.unitPrice()).isEqualTo(productDomain.getUnitPrice().toString());
    assertThat(product.createdAt()).isEqualTo(productDomain.getCreatedAt().toString());
    assertThat(product.updatedAt()).isEqualTo(productDomain.getUpdatedAt().toString());
    assertThat(product.images().size()).isEqualTo(uploadResponse.getPayload().getFirst().getImages().size());
    assertThat(product.images().getFirst()).usingRecursiveComparison().isEqualTo(uploadResponse.getPayload().getFirst().getImages().getFirst());

    verify(this.uploadService, times(1)).getProductImages(productId);
  }

  @Test
  @DisplayName("getProductsSuccess - Should successfully get products of the given parameters")
  void getProductsSuccess() {
    final String categoryName = "mouse";
    final String brandName = "logitech";
    final String modelName = "g pro";
    final List<ProductEntity> productsEntity = List.of(this.dataMock.getProductsEntity().get(0));
    final List<Product> productsDomain = List.of(this.dataMock.getProductsDomain().get(0));

    final Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));
    final Page<ProductEntity> productsPage = new PageImpl<>(productsEntity);
    final Set<String> productIds = Set.of(productsPage.getContent().get(0).getId().toString());

    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    final UploadService.ImageResponse productImage = new UploadService.ImageResponse(
      productsEntity.get(0).getId().toString(),
      List.of(image1)
    );
    final var uploadResponse = new ResponsePayload.Builder<List<UploadService.ImageResponse>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Imagens dos produtos")
      .payload(List.of(productImage))
      .build();

    when(this.productRepository.findByOptionalParameters(categoryName, brandName, modelName, pagination)).thenReturn(productsPage);
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(0))).thenReturn(productsDomain.get(0));
    when(this.uploadService.getProductImages(productIds)).thenReturn(uploadResponse);

    PageResponseDTO foundProductsPage = this.productGateway.getProducts(categoryName, brandName, modelName, 0, 10);

    assertThat(foundProductsPage.currentPage()).isEqualTo(0);
    assertThat(foundProductsPage.currentElements()).isEqualTo(1);
    assertThat(foundProductsPage.totalPages()).isEqualTo(1);
    assertThat(foundProductsPage.totalElements()).isEqualTo(1L);
    assertThat(foundProductsPage.content().size()).isEqualTo(productsPage.getTotalElements());
    assertThat(foundProductsPage.content().get(0).id()).isEqualTo(productsDomain.get(0).getId().toString());
    assertThat(foundProductsPage.content().get(0).name()).isEqualTo(productsDomain.get(0).getName());
    assertThat(foundProductsPage.content().get(0).description()).isEqualTo(productsDomain.get(0).getDescription());
    assertThat(foundProductsPage.content().get(0).unitPrice()).isEqualTo(productsDomain.get(0).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(0).quantity()).isEqualTo(productsDomain.get(0).getQuantity());
    assertThat(foundProductsPage.content().get(0).createdAt()).isEqualTo(productsDomain.get(0).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(0).updatedAt()).isEqualTo(productsDomain.get(0).getUpdatedAt().toString());
    // Product found images quantity assertion
    assertThat(foundProductsPage.content().get(0).images().size()).isEqualTo(1);
    // Images of product 1 assertion
    assertThat(foundProductsPage.content().get(0).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(0).getImages().get(0));

    verify(this.productRepository, times(1)).findByOptionalParameters(categoryName, brandName, modelName, pagination);
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(0));
    verify(this.uploadService, times(1)).getProductImages(productIds);
  }

  @Test
  @DisplayName("getAllProductsSuccess - Should successfully get all products")
  void getAllProductsSuccess() {
   final List<ProductEntity> productsEntity = List.of(
     ProductEntity.mutate(this.dataMock.getProductsEntity().get(0)).id(UUID.fromString("cb7169ad-7f13-4292-aa7b-67b9b2f80913")).build(),
     ProductEntity.mutate(this.dataMock.getProductsEntity().get(1)).id(UUID.fromString("f5aaf5b3-386e-44ad-a99d-f07bee289ef1")).build(),
     ProductEntity.mutate(this.dataMock.getProductsEntity().get(0)).id(UUID.fromString("77e1afa6-dd5f-441b-b62b-b453e4bb6009")).build()
   );
   final List<Product> productsDomain = List.of(
     Product.mutate(this.dataMock.getProductsDomain().get(0)).id(UUID.fromString("cb7169ad-7f13-4292-aa7b-67b9b2f80913")).build(),
     Product.mutate(this.dataMock.getProductsDomain().get(1)).id(UUID.fromString("f5aaf5b3-386e-44ad-a99d-f07bee289ef1")).build(),
     Product.mutate(this.dataMock.getProductsDomain().get(0)).id(UUID.fromString("77e1afa6-dd5f-441b-b62b-b453e4bb6009")).build()
   );

    final Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));
    final Page<ProductEntity> productsPage = new PageImpl<>(productsEntity);
    final Set<String> productIds = Set.of(
      productsPage.getContent().get(0).getId().toString(),
      productsPage.getContent().get(1).getId().toString(),
      productsPage.getContent().get(2).getId().toString()
    );

    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    final UploadService.ImageResponse product1Image = new UploadService.ImageResponse(
      productsEntity.get(0).getId().toString(),
      List.of(image1)
    );
    final UploadService.ImageResponse product2Image = new UploadService.ImageResponse(
      productsEntity.get(1).getId().toString(),
      List.of(image1)
    );
    final UploadService.ImageResponse product3Image = new UploadService.ImageResponse(
      productsEntity.get(2).getId().toString(),
      List.of(image1)
    );
    final var uploadResponse = new ResponsePayload.Builder<List<UploadService.ImageResponse>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Imagens dos produtos")
      .payload(List.of(product1Image, product2Image, product3Image))
      .build();

    when(this.productRepository.findAll(pagination)).thenReturn(productsPage);
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(0))).thenReturn(productsDomain.get(0));
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(1))).thenReturn(productsDomain.get(1));
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(2))).thenReturn(productsDomain.get(2));
    when(this.uploadService.getProductImages(productIds)).thenReturn(uploadResponse);

    PageResponseDTO foundProductsPage = this.productGateway.getAllProducts(0, 10);

    assertThat(foundProductsPage.currentPage()).isEqualTo(0);
    assertThat(foundProductsPage.currentElements()).isEqualTo(3);
    assertThat(foundProductsPage.totalPages()).isEqualTo(1);
    assertThat(foundProductsPage.totalElements()).isEqualTo(3L);
    assertThat(foundProductsPage.content().size()).isEqualTo(productsPage.getTotalElements());

    assertThat(foundProductsPage.content().get(0).id()).isEqualTo(productsDomain.get(0).getId().toString());
    assertThat(foundProductsPage.content().get(0).name()).isEqualTo(productsDomain.get(0).getName());
    assertThat(foundProductsPage.content().get(0).description()).isEqualTo(productsDomain.get(0).getDescription());
    assertThat(foundProductsPage.content().get(0).unitPrice()).isEqualTo(productsDomain.get(0).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(0).quantity()).isEqualTo(productsDomain.get(0).getQuantity());
    assertThat(foundProductsPage.content().get(0).createdAt()).isEqualTo(productsDomain.get(0).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(0).updatedAt()).isEqualTo(productsDomain.get(0).getUpdatedAt().toString());

    assertThat(foundProductsPage.content().get(1).id()).isEqualTo(productsDomain.get(1).getId().toString());
    assertThat(foundProductsPage.content().get(1).name()).isEqualTo(productsDomain.get(1).getName());
    assertThat(foundProductsPage.content().get(1).description()).isEqualTo(productsDomain.get(1).getDescription());
    assertThat(foundProductsPage.content().get(1).unitPrice()).isEqualTo(productsDomain.get(1).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(1).quantity()).isEqualTo(productsDomain.get(1).getQuantity());
    assertThat(foundProductsPage.content().get(1).createdAt()).isEqualTo(productsDomain.get(1).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(1).updatedAt()).isEqualTo(productsDomain.get(1).getUpdatedAt().toString());

    assertThat(foundProductsPage.content().get(2).id()).isEqualTo(productsDomain.get(2).getId().toString());
    assertThat(foundProductsPage.content().get(2).name()).isEqualTo(productsDomain.get(2).getName());
    assertThat(foundProductsPage.content().get(2).description()).isEqualTo(productsDomain.get(2).getDescription());
    assertThat(foundProductsPage.content().get(2).unitPrice()).isEqualTo(productsDomain.get(2).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(2).quantity()).isEqualTo(productsDomain.get(2).getQuantity());
    assertThat(foundProductsPage.content().get(2).createdAt()).isEqualTo(productsDomain.get(2).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(2).updatedAt()).isEqualTo(productsDomain.get(2).getUpdatedAt().toString());
    // Product found images quantity assertion
    assertThat(foundProductsPage.content().get(0).images().size()).isEqualTo(1);
    assertThat(foundProductsPage.content().get(1).images().size()).isEqualTo(1);
    assertThat(foundProductsPage.content().get(2).images().size()).isEqualTo(1);
    // Images of product 1 assertion
    assertThat(foundProductsPage.content().get(0).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(0).getImages().get(0));
    // Images of product 2 assertion
    assertThat(foundProductsPage.content().get(1).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(1).getImages().get(0));
    // Images of product 3 assertion
    assertThat(foundProductsPage.content().get(2).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(2).getImages().get(0));

    verify(this.productRepository, times(1)).findAll(pagination);
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(0));
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(1));
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(2));
    verify(this.uploadService, times(1)).getProductImages(productIds);
  }

  @Test
  @DisplayName("getProductsByCategorySuccess - Should successfully get products of the given category")
  void getProductsByCategorySuccess() {
    final String categoryName = "mouse";
    final ProductEntity product2 = ProductEntity.mutate(this.dataMock.getProductsEntity().get(1))
      .id(UUID.fromString("8c4f1419-8ef8-47e9-9e18-0e9cc993debc"))
      .build();
    final List<ProductEntity> productsEntity = List.of(this.dataMock.getProductsEntity().get(0), product2);
    final List<Product> productsDomain = List.of(this.dataMock.getProductsDomain().get(0), this.dataMock.getProductsDomain().get(1));

    final Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));
    final Page<ProductEntity> productsPage = new PageImpl<>(productsEntity);
    final Set<String> productIds = Set.of(productsPage.getContent().get(0).getId().toString(), productsPage.getContent().get(1).getId().toString());

    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    final UploadService.ImageResponse product1Image = new UploadService.ImageResponse(
      productsEntity.get(0).getId().toString(),
      List.of(image1)
    );
    final UploadService.ImageResponse product2Image = new UploadService.ImageResponse(
      productsEntity.get(1).getId().toString(),
      List.of(image1)
    );
    final var uploadResponse = new ResponsePayload.Builder<List<UploadService.ImageResponse>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Imagens dos produtos")
      .payload(List.of(product1Image, product2Image))
      .build();

    when(this.productRepository.findByCategoryName(categoryName, pagination)).thenReturn(productsPage);
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(0))).thenReturn(productsDomain.get(0));
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(1))).thenReturn(productsDomain.get(1));
    when(this.uploadService.getProductImages(productIds)).thenReturn(uploadResponse);

    PageResponseDTO foundProductsPage = this.productGateway.getProductsByCategory(categoryName, 0, 10);

    assertThat(foundProductsPage.currentPage()).isEqualTo(0);
    assertThat(foundProductsPage.currentElements()).isEqualTo(2);
    assertThat(foundProductsPage.totalPages()).isEqualTo(1);
    assertThat(foundProductsPage.totalElements()).isEqualTo(2L);
    assertThat(foundProductsPage.content().size()).isEqualTo(productsPage.getTotalElements());
    assertThat(foundProductsPage.content().get(0).id()).isEqualTo(productsDomain.get(0).getId().toString());
    assertThat(foundProductsPage.content().get(0).name()).isEqualTo(productsDomain.get(0).getName());
    assertThat(foundProductsPage.content().get(0).description()).isEqualTo(productsDomain.get(0).getDescription());
    assertThat(foundProductsPage.content().get(0).unitPrice()).isEqualTo(productsDomain.get(0).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(0).quantity()).isEqualTo(productsDomain.get(0).getQuantity());
    assertThat(foundProductsPage.content().get(0).createdAt()).isEqualTo(productsDomain.get(0).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(0).updatedAt()).isEqualTo(productsDomain.get(0).getUpdatedAt().toString());
    // Product 2 assertions
    assertThat(foundProductsPage.content().get(1).id()).isEqualTo(productsDomain.get(1).getId().toString());
    assertThat(foundProductsPage.content().get(1).name()).isEqualTo(productsDomain.get(1).getName());
    assertThat(foundProductsPage.content().get(1).description()).isEqualTo(productsDomain.get(1).getDescription());
    assertThat(foundProductsPage.content().get(1).unitPrice()).isEqualTo(productsDomain.get(1).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(1).quantity()).isEqualTo(productsDomain.get(1).getQuantity());
    assertThat(foundProductsPage.content().get(1).createdAt()).isEqualTo(productsDomain.get(1).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(1).updatedAt()).isEqualTo(productsDomain.get(1).getUpdatedAt().toString());
    // Product found images quantity assertion
    assertThat(foundProductsPage.content().get(0).images().size()).isEqualTo(1);
    assertThat(foundProductsPage.content().get(1).images().size()).isEqualTo(1);
    // Images of product 1 assertion
    assertThat(foundProductsPage.content().get(0).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(0).getImages().get(0));
    // Images of product 2 assertion
    assertThat(foundProductsPage.content().get(1).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(1).getImages().get(0));

    verify(this.productRepository, times(1)).findByCategoryName(categoryName, pagination);
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(0));
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(1));
    verify(this.uploadService, times(1)).getProductImages(productIds);
  }

  @Test
  @DisplayName("getProductsByBrandSuccess - Should successfully get products of the given brand")
  void getProductsByBrandSuccess() {
    final String brandName = "logitech";
    final ProductEntity product2 = ProductEntity.mutate(this.dataMock.getProductsEntity().get(1))
      .id(UUID.fromString("8c4f1419-8ef8-47e9-9e18-0e9cc993debc"))
      .build();
    final List<ProductEntity> productsEntity = List.of(this.dataMock.getProductsEntity().get(0), product2);
    final List<Product> productsDomain = List.of(this.dataMock.getProductsDomain().get(0), this.dataMock.getProductsDomain().get(1));

    final Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));
    final Page<ProductEntity> productsPage = new PageImpl<>(productsEntity);
    final Set<String> productIds = Set.of(productsPage.getContent().get(0).getId().toString(), productsPage.getContent().get(1).getId().toString());

    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    final UploadService.ImageResponse product1Image = new UploadService.ImageResponse(
      productsEntity.get(0).getId().toString(),
      List.of(image1)
    );
    final UploadService.ImageResponse product2Image = new UploadService.ImageResponse(
      productsEntity.get(1).getId().toString(),
      List.of(image1)
    );
    final var uploadResponse = new ResponsePayload.Builder<List<UploadService.ImageResponse>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Imagens dos produtos")
      .payload(List.of(product1Image, product2Image))
      .build();

    when(this.productRepository.findByBrandName(brandName, pagination)).thenReturn(productsPage);
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(0))).thenReturn(productsDomain.get(0));
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(1))).thenReturn(productsDomain.get(1));
    when(this.uploadService.getProductImages(productIds)).thenReturn(uploadResponse);

    PageResponseDTO foundProductsPage = this.productGateway.getProductsByBrand(brandName, 0, 10);

    assertThat(foundProductsPage.currentPage()).isEqualTo(0);
    assertThat(foundProductsPage.currentElements()).isEqualTo(2);
    assertThat(foundProductsPage.totalPages()).isEqualTo(1);
    assertThat(foundProductsPage.totalElements()).isEqualTo(2L);
    assertThat(foundProductsPage.content().size()).isEqualTo(productsPage.getTotalElements());
    assertThat(foundProductsPage.content().get(0).id()).isEqualTo(productsDomain.get(0).getId().toString());
    assertThat(foundProductsPage.content().get(0).name()).isEqualTo(productsDomain.get(0).getName());
    assertThat(foundProductsPage.content().get(0).description()).isEqualTo(productsDomain.get(0).getDescription());
    assertThat(foundProductsPage.content().get(0).unitPrice()).isEqualTo(productsDomain.get(0).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(0).quantity()).isEqualTo(productsDomain.get(0).getQuantity());
    assertThat(foundProductsPage.content().get(0).createdAt()).isEqualTo(productsDomain.get(0).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(0).updatedAt()).isEqualTo(productsDomain.get(0).getUpdatedAt().toString());
    // Product 2 assertions
    assertThat(foundProductsPage.content().get(1).id()).isEqualTo(productsDomain.get(1).getId().toString());
    assertThat(foundProductsPage.content().get(1).name()).isEqualTo(productsDomain.get(1).getName());
    assertThat(foundProductsPage.content().get(1).description()).isEqualTo(productsDomain.get(1).getDescription());
    assertThat(foundProductsPage.content().get(1).unitPrice()).isEqualTo(productsDomain.get(1).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(1).quantity()).isEqualTo(productsDomain.get(1).getQuantity());
    assertThat(foundProductsPage.content().get(1).createdAt()).isEqualTo(productsDomain.get(1).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(1).updatedAt()).isEqualTo(productsDomain.get(1).getUpdatedAt().toString());
    // Product found images quantity assertion
    assertThat(foundProductsPage.content().get(0).images().size()).isEqualTo(1);
    assertThat(foundProductsPage.content().get(1).images().size()).isEqualTo(1);
    // Images of product 1 assertion
    assertThat(foundProductsPage.content().get(0).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(0).getImages().get(0));
    // Images of product 2 assertion
    assertThat(foundProductsPage.content().get(1).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(1).getImages().get(0));

    verify(this.productRepository, times(1)).findByBrandName(brandName, pagination);
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(0));
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(1));
    verify(this.uploadService, times(1)).getProductImages(productIds);
  }

  @Test
  @DisplayName("getProductsByModelSuccess - Should successfully get products of the given model and brand")
  void getProductsByModelSuccess() {
    final String modelName = "g pro";
    final String brandName = "logitech";
    final List<ProductEntity> productsEntity = List.of(this.dataMock.getProductsEntity().get(0));
    final List<Product> productsDomain = List.of(this.dataMock.getProductsDomain().get(0));

    final Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));
    final Page<ProductEntity> productsPage = new PageImpl<>(productsEntity);
    final Set<String> productIds = Set.of(productsPage.getContent().get(0).getId().toString());

    ImageFileDTO image1 = new ImageFileDTO(
      "01",
      "image1",
      "imagePath",
      "image/png",
      "123456",
      "image1",
      "thumbnail",
      "01",
      "anything",
      "anything"
    );
    final UploadService.ImageResponse productImage = new UploadService.ImageResponse(
      productsEntity.get(0).getId().toString(),
      List.of(image1)
    );
    final var uploadResponse = new ResponsePayload.Builder<List<UploadService.ImageResponse>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Imagens dos produtos")
      .payload(List.of(productImage))
      .build();

    when(this.productRepository.findByModelNameAndBrandName(modelName, brandName, pagination)).thenReturn(productsPage);
    when(this.productEntityMapper.toDomain(productsPage.getContent().get(0))).thenReturn(productsDomain.get(0));
    when(this.uploadService.getProductImages(productIds)).thenReturn(uploadResponse);

    PageResponseDTO foundProductsPage = this.productGateway.getProductsByModel(modelName, brandName, 0, 10);

    assertThat(foundProductsPage.currentPage()).isEqualTo(0);
    assertThat(foundProductsPage.currentElements()).isEqualTo(1);
    assertThat(foundProductsPage.totalPages()).isEqualTo(1);
    assertThat(foundProductsPage.totalElements()).isEqualTo(1L);
    assertThat(foundProductsPage.content().size()).isEqualTo(productsPage.getTotalElements());
    assertThat(foundProductsPage.content().get(0).id()).isEqualTo(productsDomain.get(0).getId().toString());
    assertThat(foundProductsPage.content().get(0).name()).isEqualTo(productsDomain.get(0).getName());
    assertThat(foundProductsPage.content().get(0).description()).isEqualTo(productsDomain.get(0).getDescription());
    assertThat(foundProductsPage.content().get(0).unitPrice()).isEqualTo(productsDomain.get(0).getUnitPrice().toString());
    assertThat(foundProductsPage.content().get(0).quantity()).isEqualTo(productsDomain.get(0).getQuantity());
    assertThat(foundProductsPage.content().get(0).createdAt()).isEqualTo(productsDomain.get(0).getCreatedAt().toString());
    assertThat(foundProductsPage.content().get(0).updatedAt()).isEqualTo(productsDomain.get(0).getUpdatedAt().toString());
    // Product found images quantity assertion
    assertThat(foundProductsPage.content().get(0).images().size()).isEqualTo(1);
    // Images of product 1 assertion
    assertThat(foundProductsPage.content().get(0).images().get(0)).usingRecursiveComparison()
      .isEqualTo(uploadResponse.getPayload().get(0).getImages().get(0));

    verify(this.productRepository, times(1)).findByModelNameAndBrandName(modelName, brandName, pagination);
    verify(this.productEntityMapper, times(1)).toDomain(productsEntity.get(0));
    verify(this.uploadService, times(1)).getProductImages(productIds);
  }

  @Test
  @DisplayName("deleteProductSuccess - Should successfully delete a product and return it")
  void deleteProductSuccess() {
    final Product productDomain = this.dataMock.getProductsDomain().getFirst();
    final ProductEntity productEntity = this.dataMock.getProductsEntity().getFirst();
    final var deleteImagesResponse = new ResponsePayload.Builder<UploadService.DeleteImagesResponse>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Images excluídas com sucesso")
      .payload(new UploadService.DeleteImagesResponse(productEntity.getId().toString(), 1))
      .build();

    when(this.productEntityMapper.toEntity(productDomain)).thenReturn(productEntity);
    when(this.uploadService.deleteImages(productEntity.getId().toString())).thenReturn(deleteImagesResponse);
    doNothing().when(this.productRepository).delete(productEntity);

    Product deletedProduct = this.productGateway.deleteProduct(productDomain);

    assertThat(deletedProduct.getId()).isEqualTo(productDomain.getId());
    assertThat(deletedProduct.getName()).isEqualTo(productDomain.getName());
    assertThat(deletedProduct.getDescription()).isEqualTo(productDomain.getDescription());
    assertThat(deletedProduct.getQuantity()).isEqualTo(productDomain.getQuantity());
    assertThat(deletedProduct.getUnitPrice().toString()).isEqualTo(productDomain.getUnitPrice().toString());
    assertThat(deletedProduct.getCreatedAt()).isEqualTo(productDomain.getCreatedAt());
    assertThat(deletedProduct.getUpdatedAt()).isEqualTo(productDomain.getUpdatedAt());
    assertThat(deletedProduct.getCategory()).usingRecursiveComparison().isEqualTo(productDomain.getCategory());
    assertThat(deletedProduct.getBrand()).usingRecursiveComparison().isEqualTo(productDomain.getBrand());
    assertThat(deletedProduct.getModel()).usingRecursiveComparison().isEqualTo(productDomain.getModel());

    verify(this.productEntityMapper, times(1)).toEntity(productDomain);
    verify(this.uploadService, times(1)).deleteImages(productEntity.getId().toString());
    verify(this.productRepository, times(1)).delete(productEntity);
  }
}
