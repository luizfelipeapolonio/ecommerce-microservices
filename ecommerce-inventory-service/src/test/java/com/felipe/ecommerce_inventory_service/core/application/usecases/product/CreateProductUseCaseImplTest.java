package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ImageFileDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ProductAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.CreateProductUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UploadFileImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class CreateProductUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  @Mock
  private GetCategoryByIdUseCase getCategoryByIdUseCase;

  @Mock
  private GetBrandByIdUseCase getBrandByIdUseCase;

  @Mock
  private GetModelByIdUseCase getModelByIdUseCase;

  private CreateProductUseCaseImpl createProductUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.createProductUseCase = new CreateProductUseCaseImpl(
      this.productGateway,
      this.getCategoryByIdUseCase,
      this.getBrandByIdUseCase,
      this.getModelByIdUseCase
    );
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createProductSuccess - Should successfully create a product and return it")
  void createProductSuccess() {
    Product product = this.dataMock.getProductsDomain().getFirst();
    Category category = this.dataMock.getCategoriesDomain().get(4);
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    Model model = this.dataMock.getModelsDomain().getFirst();
    CreateProductDomainDTO productDTO = new CreateProductDTO(product);
    UploadFile[] files = {
      UploadFileImpl.builder().originalFileName("image1").build(),
      UploadFileImpl.builder().originalFileName("image2").build()
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
    ProductResponseDTO createdProductDTO = new CreatedProductDTO(product, List.of(image1));
    ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);

    when(this.productGateway.findProductByName(productDTO.name())).thenReturn(Optional.empty());
    when(this.getCategoryByIdUseCase.execute(productDTO.categoryId())).thenReturn(category);
    when(this.getBrandByIdUseCase.execute(productDTO.brandId())).thenReturn(brand);
    when(this.getModelByIdUseCase.execute(productDTO.modelId())).thenReturn(model);
    when(this.productGateway.createProduct(productCaptor.capture(), eq(files))).thenReturn(createdProductDTO);

    ProductResponseDTO productResponse = this.createProductUseCase.execute(productDTO, files);

    // Argument captor assertions
    assertThat(productCaptor.getValue().getName()).isEqualTo(productDTO.name());
    assertThat(productCaptor.getValue().getDescription()).isEqualTo(productDTO.description());
    assertThat(productCaptor.getValue().getQuantity()).isEqualTo(productDTO.quantity());
    assertThat(productCaptor.getValue().getCategory().getId()).isEqualTo(productDTO.categoryId());
    assertThat(productCaptor.getValue().getBrand().getId()).isEqualTo(productDTO.brandId());
    assertThat(productCaptor.getValue().getModel().getId()).isEqualTo(productDTO.modelId());
    // Product response assertions
    assertThat(productResponse.id()).isEqualTo(createdProductDTO.id());
    assertThat(productResponse.name()).isEqualTo(createdProductDTO.name());
    assertThat(productResponse.description()).isEqualTo(createdProductDTO.description());
    assertThat(productResponse.quantity()).isEqualTo(createdProductDTO.quantity());
    assertThat(productResponse.unitPrice()).isEqualTo(createdProductDTO.unitPrice());
    assertThat(productResponse.createdAt()).isEqualTo(createdProductDTO.createdAt());
    assertThat(productResponse.updatedAt()).isEqualTo(createdProductDTO.updatedAt());
    assertThat(productResponse.images().size()).isEqualTo(createdProductDTO.images().size());

    verify(this.productGateway, times(1)).findProductByName(productDTO.name());
    verify(this.getCategoryByIdUseCase, times(1)).execute(productDTO.categoryId());
    verify(this.getBrandByIdUseCase, times(1)).execute(productDTO.brandId());
    verify(this.getModelByIdUseCase, times(1)).execute(productDTO.modelId());
    verify(this.productGateway, times(1)).createProduct(any(Product.class), eq(files));
  }

  @Test
  @DisplayName("createProductFailsByProductAlreadyExists - Should throw a ProductAlreadyExistsException if the given product already exists")
  void createProductFailsByProductAlreadyExists() {
    Product product = this.dataMock.getProductsDomain().getFirst();
    CreateProductDomainDTO productDTO = new CreateProductDTO(product);
    UploadFile[] files = {
      UploadFileImpl.builder().originalFileName("image1").build(),
      UploadFileImpl.builder().originalFileName("image2").build()
    };

    when(this.productGateway.findProductByName(productDTO.name())).thenReturn(Optional.of(product));

    Exception thrown = catchException(() -> this.createProductUseCase.execute(productDTO, files));

    assertThat(thrown)
      .isExactlyInstanceOf(ProductAlreadyExistsException.class)
      .hasMessage("O produto '%s' já existe", productDTO.name());

    verify(this.productGateway, times(1)).findProductByName(productDTO.name());
    verify(this.getCategoryByIdUseCase, never()).execute(any(Long.class));
    verify(this.getBrandByIdUseCase, never()).execute(any(Long.class));
    verify(this.getModelByIdUseCase, never()).execute(any(Long.class));
    verify(this.productGateway, never()).createProduct(any(Product.class), any(UploadFile[].class));
  }

  private record CreateProductDTO(String name,
                                  String description,
                                  String unitPrice,
                                  Long quantity,
                                  Long categoryId,
                                  Long brandId,
                                  Long modelId) implements CreateProductDomainDTO {
    public CreateProductDTO(Product product) {
      this(
        product.getName(),
        product.getDescription(),
        product.getUnitPrice().toString(),
        product.getQuantity(),
        product.getCategory().getId(),
        product.getBrand().getId(),
        product.getModel().getId()
      );
    }
  }

  private record CreatedProductDTO(String id,
                                   String name,
                                   String description,
                                   String unitPrice,
                                   long quantity,
                                   boolean withDiscount,
                                   String discountType,
                                   String discountValue,
                                   String createdAt,
                                   String updatedAt,
                                   CategoryDTO category,
                                   BrandDTO brand,
                                   ModelDTO model,
                                   List<ImageFileDTO> images) implements ProductResponseDTO {
    public CreatedProductDTO(Product product, List<ImageFileDTO> images) {
      this(
        product.getId().toString(),
        product.getName(),
        product.getDescription(),
        product.getUnitPrice().toString(),
        product.getQuantity(),
        product.isItWithDiscount(),
        product.getDiscountType() == null ? null : product.getDiscountType(),
        product.getDiscountValue() == null ? null : product.getDiscountValue(),
        product.getCreatedAt().toString(),
        product.getUpdatedAt().toString(),
        new CategoryDTO(product.getCategory()),
        new BrandDTO(product.getBrand()),
        new ModelDTO(product.getModel()),
        images
      );
    }
  }
}
