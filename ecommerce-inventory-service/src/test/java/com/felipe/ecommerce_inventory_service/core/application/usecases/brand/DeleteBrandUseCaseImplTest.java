package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.DeleteBrandUseCaseImpl;
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

@ExtendWith(MockitoExtension.class)
public class DeleteBrandUseCaseImplTest {

  @Mock
  private BrandGateway brandGateway;

  private DeleteBrandUseCaseImpl deleteBrandUseCase;
  private List<Brand> brandsDomain;

  @BeforeEach
  void setUp() {
    this.deleteBrandUseCase = new DeleteBrandUseCaseImpl(this.brandGateway);
    this.brandsDomain = new DataMock().getBrandsDomain();
  }

  @Test
  @DisplayName("deleteBrandSuccess - Should successfully delete a brand and return the deleted brand")
  void deleteBrandSuccess() {
    Brand brand = this.brandsDomain.getFirst();

    when(this.brandGateway.findBrandById(brand.getId())).thenReturn(Optional.of(brand));
    when(this.brandGateway.deleteBrand(brand)).thenReturn(brand);

    Brand deletedBrand = this.deleteBrandUseCase.execute(brand.getId());

    assertThat(deletedBrand.getId()).isEqualTo(brand.getId());
    assertThat(deletedBrand.getName()).isEqualTo(brand.getName());
    assertThat(deletedBrand.getDescription()).isEqualTo(brand.getDescription());
    assertThat(deletedBrand.getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(deletedBrand.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandGateway, times(1)).findBrandById(brand.getId());
    verify(this.brandGateway, times(1)).deleteBrand(brand);
  }

  @Test
  @DisplayName("deleteBrandFailsByBrandNotFound - Should throw a DataNotFoundException if brand is not found")
  void deleteBrandFailsByBrandNotFound() {
    final Long brandId = 1L;

    when(this.brandGateway.findBrandById(brandId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deleteBrandUseCase.execute(brandId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Marca de id '%s' não encontrada", brandId);

    verify(this.brandGateway, times(1)).findBrandById(brandId);
    verify(this.brandGateway, never()).deleteBrand(any(Brand.class));
  }
}
