package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
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
public class ModelEntityMapperTest {

  @Mock
  private BrandEntityMapper brandEntityMapper;

  @InjectMocks
  private ModelEntityMapper modelEntityMapper;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertEntityToDomain - Should successfully convert a ModelEntity to Model domain")
  void convertEntityToDomain() {
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();
    Brand brand = this.dataMock.getBrandsDomain().getFirst();

    when(this.brandEntityMapper.toDomain(modelEntity.getBrand())).thenReturn(brand);

    Model convertedModel = this.modelEntityMapper.toDomain(modelEntity);

    assertThat(convertedModel.getId()).isEqualTo(modelEntity.getId());
    assertThat(convertedModel.getName()).isEqualTo(modelEntity.getName());
    assertThat(convertedModel.getDescription()).isEqualTo(modelEntity.getDescription());
    assertThat(convertedModel.getCreatedAt()).isEqualTo(modelEntity.getCreatedAt());
    assertThat(convertedModel.getUpdatedAt()).isEqualTo(modelEntity.getUpdatedAt());
    assertThat(convertedModel.getBrand().getId()).isEqualTo(brand.getId());
    assertThat(convertedModel.getBrand().getName()).isEqualTo(brand.getName());
    assertThat(convertedModel.getBrand().getDescription()).isEqualTo(brand.getDescription());
    assertThat(convertedModel.getBrand().getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(convertedModel.getBrand().getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandEntityMapper, times(1)).toDomain(modelEntity.getBrand());
  }

  @Test
  @DisplayName("convertDomainToEntity - Should successfully convert a Model domain to ModelEntity")
  void convertDomainToEntity() {
    Model model = this.dataMock.getModelsDomain().getFirst();
    BrandEntity brandEntity = this.dataMock.getBrandsEntity().getFirst();

    when(this.brandEntityMapper.toEntity(model.getBrand())).thenReturn(brandEntity);

    ModelEntity convertedModel = this.modelEntityMapper.toEntity(model);

    assertThat(convertedModel.getId()).isEqualTo(model.getId());
    assertThat(convertedModel.getName()).isEqualTo(model.getName());
    assertThat(convertedModel.getDescription()).isEqualTo(model.getDescription());
    assertThat(convertedModel.getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(convertedModel.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    assertThat(convertedModel.getBrand().getId()).isEqualTo(brandEntity.getId());
    assertThat(convertedModel.getBrand().getName()).isEqualTo(brandEntity.getName());
    assertThat(convertedModel.getBrand().getDescription()).isEqualTo(brandEntity.getDescription());
    assertThat(convertedModel.getBrand().getCreatedAt()).isEqualTo(brandEntity.getCreatedAt());
    assertThat(convertedModel.getBrand().getUpdatedAt()).isEqualTo(brandEntity.getUpdatedAt());

    verify(this.brandEntityMapper, times(1)).toEntity(model.getBrand());
  }
}
