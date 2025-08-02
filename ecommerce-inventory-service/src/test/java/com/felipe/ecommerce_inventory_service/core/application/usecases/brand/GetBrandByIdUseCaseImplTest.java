package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.GetBrandByIdUseCaseImpl;
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

@ExtendWith(MockitoExtension.class)
public class GetBrandByIdUseCaseImplTest {

  @Mock
  private BrandGateway brandGateway;

  private GetBrandByIdUseCaseImpl getBrandByIdUseCase;
  private List<Brand> brandsDomain;

  @BeforeEach
  void setUp() {
    this.getBrandByIdUseCase = new GetBrandByIdUseCaseImpl(this.brandGateway);
    this.brandsDomain = new DataMock().getBrandsDomain();
  }

  @Test
  @DisplayName("getBrandByIdSuccess - Should successfully find a brand by id and return it")
  void getBrandByIdSuccess() {
    Brand brand = this.brandsDomain.getFirst();

    when(this.brandGateway.findBrandById(brand.getId())).thenReturn(Optional.of(brand));

    Brand foundBrand = this.getBrandByIdUseCase.execute(brand.getId());

    assertThat(foundBrand.getId()).isEqualTo(brand.getId());
    assertThat(foundBrand.getName()).isEqualTo(brand.getName());
    assertThat(foundBrand.getDescription()).isEqualTo(brand.getDescription());
    assertThat(foundBrand.getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(foundBrand.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandGateway, times(1)).findBrandById(brand.getId());
  }

  @Test
  @DisplayName("getBrandByIdFailsByBrandNotFound - Should throw a DataNotFoundException if the brand is not found")
  void getBrandByIdFailsByBrandNotFound() {
    final Long brandId = 1L;

    when(this.brandGateway.findBrandById(brandId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getBrandByIdUseCase.execute(brandId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Marca de id '%s' não encontrada", brandId);

    verify(this.brandGateway, times(1)).findBrandById(brandId);
  }
}
