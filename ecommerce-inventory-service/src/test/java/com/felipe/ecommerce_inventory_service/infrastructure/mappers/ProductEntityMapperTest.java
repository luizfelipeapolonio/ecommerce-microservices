package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ProductEntityMapperTest {

  @Mock
  private CategoryEntityMapper categoryEntityMapper;

  @Mock
  private BrandEntityMapper brandEntityMapper;

  @Mock
  private ModelEntityMapper modelEntityMapper;

  @InjectMocks
  private ProductEntityMapper productEntityMapper;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertProductDomainToProductEntity - Should successfully convert a Product domain to ProductEntity")
  void convertProductDomainToProductEntity() {
    Product product = this.dataMock.getProductsDomain().getFirst();
    CategoryEntity categoryEntity = this.dataMock.getCategoriesEntity().get(4);
    BrandEntity brandEntity = this.dataMock.getBrandsEntity().getFirst();
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();

    when(this.categoryEntityMapper.toEntity(product.getCategory())).thenReturn(categoryEntity);
    when(this.brandEntityMapper.toEntity(product.getBrand())).thenReturn(brandEntity);
    when(this.modelEntityMapper.toEntity(product.getModel())).thenReturn(modelEntity);

    ProductEntity convertedProduct = this.productEntityMapper.toEntity(product);

    assertThat(convertedProduct.getId()).isEqualTo(product.getId());
    assertThat(convertedProduct.getName()).isEqualTo(product.getName());
    assertThat(convertedProduct.getDescription()).isEqualTo(product.getDescription());
    assertThat(convertedProduct.getQuantity()).isEqualTo(product.getQuantity());
    assertThat(convertedProduct.getUnitPrice()).isEqualTo(product.getUnitPrice());
    assertThat(convertedProduct.getCreatedAt()).isEqualTo(product.getCreatedAt());
    assertThat(convertedProduct.getUpdatedAt()).isEqualTo(product.getUpdatedAt());
    assertThat(convertedProduct.getCategory()).usingRecursiveComparison().isEqualTo(categoryEntity);
    assertThat(convertedProduct.getBrand()).usingRecursiveComparison().isEqualTo(brandEntity);
    assertThat(convertedProduct.getModel()).usingRecursiveComparison().isEqualTo(modelEntity);

    verify(this.categoryEntityMapper, times(1)).toEntity(product.getCategory());
    verify(this.brandEntityMapper, times(1)).toEntity(product.getBrand());
    verify(this.modelEntityMapper, times(1)).toEntity(product.getModel());
  }

  @Test
  @DisplayName("convertProductEntityToProductDomain - Should successfully convert a ProductEntity to Product domain")
  void convertProductEntityToProductDomain() {
    ProductEntity productEntity = this.dataMock.getProductsEntity().getFirst();
    Category category = this.dataMock.getCategoriesDomain().get(4);
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    Model model = this.dataMock.getModelsDomain().getFirst();

    when(this.categoryEntityMapper.toDomain(productEntity.getCategory())).thenReturn(category);
    when(this.brandEntityMapper.toDomain(productEntity.getBrand())).thenReturn(brand);
    when(this.modelEntityMapper.toDomain(productEntity.getModel())).thenReturn(model);

    Product convertedProduct = this.productEntityMapper.toDomain(productEntity);

    assertThat(convertedProduct.getId()).isEqualTo(productEntity.getId());
    assertThat(convertedProduct.getName()).isEqualTo(productEntity.getName());
    assertThat(convertedProduct.getDescription()).isEqualTo(productEntity.getDescription());
    assertThat(convertedProduct.getQuantity()).isEqualTo(productEntity.getQuantity());
    assertThat(convertedProduct.getUnitPrice()).isEqualTo(productEntity.getUnitPrice());
    assertThat(convertedProduct.getCreatedAt()).isEqualTo(productEntity.getCreatedAt());
    assertThat(convertedProduct.getUpdatedAt()).isEqualTo(productEntity.getUpdatedAt());
    assertThat(convertedProduct.getCategory()).usingRecursiveComparison().isEqualTo(category);
    assertThat(convertedProduct.getBrand()).usingRecursiveComparison().isEqualTo(brand);
    assertThat(convertedProduct.getModel()).usingRecursiveComparison().isEqualTo(model);

    verify(this.categoryEntityMapper, times(1)).toDomain(productEntity.getCategory());
    verify(this.brandEntityMapper, times(1)).toDomain(productEntity.getBrand());
    verify(this.modelEntityMapper, times(1)).toDomain(productEntity.getModel());
  }
}
