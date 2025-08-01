package com.felipe.ecommerce_inventory_service.infrastructure.mappers;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class BrandEntityMapperTest {

  @Spy
  private BrandEntityMapper brandEntityMapper;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("convertDomainToEntity - Should convert a Brand domain to BrandEntity")
  void convertDomainToEntity() {
    Brand brand = this.dataMock.getBrandsDomain().getFirst();

    BrandEntity convertedBrand = this.brandEntityMapper.toEntity(brand);

    assertThat(convertedBrand.getId()).isEqualTo(brand.getId());
    assertThat(convertedBrand.getName()).isEqualTo(brand.getName());
    assertThat(convertedBrand.getDescription()).isEqualTo(brand.getDescription());
    assertThat(convertedBrand.getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(convertedBrand.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandEntityMapper, times(1)).toEntity(brand);
  }

  @Test
  @DisplayName("convertEntityToDomain - Should successfully convert a BrandEntity to a Brand domain")
  void convertEntityToDomain() {
    BrandEntity brand = this.dataMock.getBrandsEntity().getFirst();

    Brand convertedBrand = this.brandEntityMapper.toDomain(brand);

    assertThat(convertedBrand.getId()).isEqualTo(brand.getId());
    assertThat(convertedBrand.getName()).isEqualTo(brand.getName());
    assertThat(convertedBrand.getDescription()).isEqualTo(brand.getDescription());
    assertThat(convertedBrand.getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(convertedBrand.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandEntityMapper, times(1)).toDomain(brand);
  }
}
