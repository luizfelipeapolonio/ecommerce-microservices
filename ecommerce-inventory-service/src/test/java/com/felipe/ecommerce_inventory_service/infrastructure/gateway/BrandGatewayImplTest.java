package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.BrandEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.BrandRepository;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class BrandGatewayImplTest {

  @Mock
  private BrandRepository brandRepository;

  @Mock
  private BrandEntityMapper brandEntityMapper;

  @InjectMocks
  private BrandGatewayImpl brandGateway;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createBrandSuccess - Should successfully create a brand and return it")
  void createBrandSuccess() {
    Brand brandDomain = this.dataMock.getBrandsDomain().getFirst();
    BrandEntity brandEntity = this.dataMock.getBrandsEntity().getFirst();
    ArgumentCaptor<BrandEntity> entityCaptor = ArgumentCaptor.forClass(BrandEntity.class);

    when(this.brandRepository.save(entityCaptor.capture())).thenReturn(brandEntity);
    when(this.brandEntityMapper.toDomain(brandEntity)).thenReturn(brandDomain);

    Brand createdBrand = this.brandGateway.createBrand(brandDomain.getName(), brandDomain.getDescription());

    // argument captor assertions
    assertThat(entityCaptor.getValue().getName()).isEqualTo(brandDomain.getName());
    assertThat(entityCaptor.getValue().getDescription()).isEqualTo(brandDomain.getDescription());
    // created brand assertions
    assertThat(createdBrand.getId()).isEqualTo(brandDomain.getId());
    assertThat(createdBrand.getName()).isEqualTo(brandDomain.getName());
    assertThat(createdBrand.getDescription()).isEqualTo(brandDomain.getDescription());
    assertThat(createdBrand.getCreatedAt()).isEqualTo(brandDomain.getCreatedAt());
    assertThat(createdBrand.getUpdatedAt()).isEqualTo(brandDomain.getUpdatedAt());

    verify(this.brandRepository, times(1)).save(any(BrandEntity.class));
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntity);
  }
}
