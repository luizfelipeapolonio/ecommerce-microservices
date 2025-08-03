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

import java.util.List;
import java.util.Optional;

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

  @Test
  @DisplayName("getBrandByNameSuccess - Should successfully find a brand by name and return an Optional of Brand")
  void getBrandByNameSuccess() {
    BrandEntity brandEntity = this.dataMock.getBrandsEntity().getFirst();
    Brand brandDomain = this.dataMock.getBrandsDomain().getFirst();

    when(this.brandRepository.findByName(brandEntity.getName())).thenReturn(Optional.of(brandEntity));
    when(this.brandEntityMapper.toDomain(brandEntity)).thenReturn(brandDomain);

    Optional<Brand> brand = this.brandGateway.findBrandByName(brandEntity.getName());

    assertThat(brand).isPresent();
    assertThat(brand.get().getId()).isEqualTo(brandDomain.getId());
    assertThat(brand.get().getName()).isEqualTo(brandDomain.getName());
    assertThat(brand.get().getDescription()).isEqualTo(brandDomain.getDescription());
    assertThat(brand.get().getCreatedAt()).isEqualTo(brandDomain.getCreatedAt());
    assertThat(brand.get().getUpdatedAt()).isEqualTo(brandDomain.getUpdatedAt());

    verify(this.brandRepository, times(1)).findByName(brandEntity.getName());
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntity);
  }

  @Test
  @DisplayName("getBrandByIdSuccess - Should successfully find a brand by id and return an Optional of Brand")
  void getBrandByIdSuccess() {
    BrandEntity brandEntity = this.dataMock.getBrandsEntity().getFirst();
    Brand brandDomain = this.dataMock.getBrandsDomain().getFirst();

    when(this.brandRepository.findById(brandEntity.getId())).thenReturn(Optional.of(brandEntity));
    when(this.brandEntityMapper.toDomain(brandEntity)).thenReturn(brandDomain);

    Optional<Brand> brand = this.brandGateway.findBrandById(brandEntity.getId());

    assertThat(brand).isPresent();
    assertThat(brand.get().getId()).isEqualTo(brandDomain.getId());
    assertThat(brand.get().getName()).isEqualTo(brandDomain.getName());
    assertThat(brand.get().getDescription()).isEqualTo(brandDomain.getDescription());
    assertThat(brand.get().getCreatedAt()).isEqualTo(brandDomain.getCreatedAt());
    assertThat(brand.get().getUpdatedAt()).isEqualTo(brandDomain.getUpdatedAt());

    verify(this.brandRepository, times(1)).findById(brandEntity.getId());
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntity);
  }

  @Test
  @DisplayName("getAllBrandsReturnsFilledList - Should successfully find all brands and return it")
  void getAllBrandsReturnsFilledList() {
    List<BrandEntity> brandEntities = this.dataMock.getBrandsEntity();
    List<Brand> brandsDomain = this.dataMock.getBrandsDomain();

    when(this.brandRepository.findAll()).thenReturn(brandEntities);
    when(this.brandEntityMapper.toDomain(brandEntities.get(0))).thenReturn(brandsDomain.get(0));
    when(this.brandEntityMapper.toDomain(brandEntities.get(1))).thenReturn(brandsDomain.get(1));
    when(this.brandEntityMapper.toDomain(brandEntities.get(2))).thenReturn(brandsDomain.get(2));

    List<Brand> brands = this.brandGateway.getAllBrands();

    assertThat(brands.size()).isEqualTo(brandEntities.size());
    assertThat(brands.stream().map(Brand::getId).toList())
      .containsExactlyInAnyOrderElementsOf(brandsDomain.stream().map(Brand::getId).toList());

    verify(this.brandRepository, times(1)).findAll();
    verify(this.brandEntityMapper, times(3)).toDomain(any(BrandEntity.class));
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntities.get(0));
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntities.get(1));
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntities.get(2));
  }

  @Test
  @DisplayName("updateBrandSuccess - Should successfully update a brand and return it")
  void updateBrandSuccess() {
    Brand brandDomain = this.dataMock.getBrandsDomain().getFirst();
    BrandEntity brandEntity = this.dataMock.getBrandsEntity().getFirst();
    final String updatedName = "updated name";
    final String updatedDescription = "updated description";
    ArgumentCaptor<BrandEntity> entityCaptor = ArgumentCaptor.forClass(BrandEntity.class);

    when(this.brandEntityMapper.toEntity(brandDomain)).thenReturn(brandEntity);
    when(this.brandRepository.save(entityCaptor.capture())).thenReturn(brandEntity);
    when(this.brandEntityMapper.toDomain(brandEntity)).thenReturn(brandDomain);

    Brand updatedBrand = this.brandGateway.updateBrand(brandDomain, updatedName, updatedDescription);

    // argument captor assertions
    assertThat(entityCaptor.getValue().getName()).isEqualTo(updatedName);
    assertThat(entityCaptor.getValue().getDescription()).isEqualTo(updatedDescription);
    // updated brand assertions
    assertThat(updatedBrand.getId()).isEqualTo(brandDomain.getId());
    assertThat(updatedBrand.getName()).isEqualTo(brandDomain.getName());
    assertThat(updatedBrand.getDescription()).isEqualTo(brandDomain.getDescription());
    assertThat(updatedBrand.getCreatedAt()).isEqualTo(brandDomain.getCreatedAt());
    assertThat(updatedBrand.getUpdatedAt()).isEqualTo(brandDomain.getUpdatedAt());

    verify(this.brandEntityMapper, times(1)).toEntity(brandDomain);
    verify(this.brandRepository, times(1)).save(any(BrandEntity.class));
    verify(this.brandEntityMapper, times(1)).toDomain(brandEntity);
  }
}
