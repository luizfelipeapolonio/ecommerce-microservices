package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.BrandAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.UpdateBrandUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class UpdateBrandUseCaseImplTest {

  @Mock
  private BrandGateway brandGateway;

  private UpdateBrandUseCaseImpl updateBrandUseCase;
  private List<Brand> brandsDomain;

  @BeforeEach
  void setUp() {
    this.updateBrandUseCase = new UpdateBrandUseCaseImpl(this.brandGateway);
    this.brandsDomain = new DataMock().getBrandsDomain();
  }

  @Test
  @DisplayName("updateBrandSuccess - Should successfully update a brand and return it")
  void updateBrandSuccess() {
    Brand brand = this.brandsDomain.getFirst();
    final String updatedName = "updated name";
    final String updatedDescription = "An updated description";

    when(this.brandGateway.findBrandById(brand.getId())).thenReturn(Optional.of(brand));
    when(this.brandGateway.findBrandByName(updatedName)).thenReturn(Optional.empty());
    when(this.brandGateway.updateBrand(brand, updatedName, updatedDescription)).thenReturn(brand);

    Brand updatedBrand = this.updateBrandUseCase.execute(brand.getId(), updatedName, updatedDescription);

    assertThat(updatedBrand.getId()).isEqualTo(brand.getId());
    assertThat(updatedBrand.getName()).isEqualTo(brand.getName());
    assertThat(updatedBrand.getDescription()).isEqualTo(brand.getDescription());
    assertThat(updatedBrand.getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(updatedBrand.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandGateway, times(1)).findBrandById(brand.getId());
    verify(this.brandGateway, times(1)).findBrandByName(updatedName);
    verify(this.brandGateway, times(1)).updateBrand(brand, updatedName, updatedDescription);
  }

  @Test
  @DisplayName("updateBrandFailsByBrandNotFound - Should throw a DataNotFoundException if brand is not found")
  void updateBrandFailsByBrandNotFound() {
    final Long brandId = 1L;

    when(this.brandGateway.findBrandById(brandId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateBrandUseCase.execute(brandId, "anything", "anything"));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Marca de id '%s' não encontrada", brandId);

    verify(this.brandGateway, times(1)).findBrandById(brandId);
    verify(this.brandGateway, never()).findBrandByName(anyString());
    verify(this.brandGateway, never()).updateBrand(any(Brand.class), anyString(), anyString());
  }

  @Test
  @DisplayName("updateBrandFailsByBrandAlreadyExists - Should throw a BrandAlreadyExistsException if the given brand name already exists")
  void updateBrandFailsByBrandAlreadyExists() {
    Brand brand = this.brandsDomain.getFirst();
    final String updatedName = "logitech";

    when(this.brandGateway.findBrandById(brand.getId())).thenReturn(Optional.of(brand));
    when(this.brandGateway.findBrandByName(updatedName)).thenReturn(Optional.of(brand));

    Exception thrown = catchException(() -> this.updateBrandUseCase.execute(brand.getId(), updatedName, "anything"));

    assertThat(thrown)
      .isExactlyInstanceOf(BrandAlreadyExistsException.class)
      .hasMessage("A marca '%s' já existe", updatedName);

    verify(this.brandGateway, times(1)).findBrandById(brand.getId());
    verify(this.brandGateway, times(1)).findBrandByName(updatedName);
    verify(this.brandGateway, never()).updateBrand(any(Brand.class), anyString(), anyString());
  }
}
