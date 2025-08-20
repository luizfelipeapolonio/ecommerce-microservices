package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UploadFileImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.product.ProductDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    CreateProductResponseDTO createdProduct = this.productGateway.createProduct(product, files);

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
}
