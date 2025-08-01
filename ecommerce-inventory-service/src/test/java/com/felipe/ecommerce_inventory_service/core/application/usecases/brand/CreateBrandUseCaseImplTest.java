package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.BrandAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.brand.CreateBrandUseCaseImpl;
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
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
public class CreateBrandUseCaseImplTest {

  @Mock
  private BrandGateway brandGateway;

  private CreateBrandUseCaseImpl createBrandUseCase;
  private List<Brand> brandsDomain;

  @BeforeEach
  void setUp() {
    this.createBrandUseCase = new CreateBrandUseCaseImpl(this.brandGateway);
    this.brandsDomain = new DataMock().getBrandsDomain();
  }

  @Test
  @DisplayName("createBrandSuccess - Should successfully create a brand and return it")
  void createBrandSuccess() {
    Brand brand = this.brandsDomain.getFirst();

    when(this.brandGateway.findBrandByName(brand.getName())).thenReturn(Optional.empty());
    when(this.brandGateway.createBrand(brand.getName(), brand.getDescription())).thenReturn(brand);

    Brand createdBrand = this.createBrandUseCase.execute(brand.getName(), brand.getDescription());

    assertThat(createdBrand.getId()).isEqualTo(brand.getId());
    assertThat(createdBrand.getName()).isEqualTo(brand.getName());
    assertThat(createdBrand.getDescription()).isEqualTo(brand.getDescription());
    assertThat(createdBrand.getCreatedAt()).isEqualTo(brand.getCreatedAt());
    assertThat(createdBrand.getUpdatedAt()).isEqualTo(brand.getUpdatedAt());

    verify(this.brandGateway, times(1)).findBrandByName(brand.getName());
    verify(this.brandGateway, times(1)).createBrand(brand.getName(), brand.getDescription());
  }

  @Test
  @DisplayName("createBrandFailsByBrandAlreadyExists - Should throw a BrandAlreadyExistsException if brand already exists")
  void createBrandFailsByBrandAlreadyExists() {
    Brand brand = this.brandsDomain.getFirst();

    when(this.brandGateway.findBrandByName(brand.getName())).thenReturn(Optional.of(brand));

    Exception thrown = catchException(() -> this.createBrandUseCase.execute(brand.getName(), brand.getDescription()));

    assertThat(thrown)
      .isExactlyInstanceOf(BrandAlreadyExistsException.class)
      .hasMessage("A marca '%s' já existe", brand.getName());

    verify(this.brandGateway, times(1)).findBrandByName(brand.getName());
    verify(this.brandGateway, never()).createBrand(anyString(), anyString());
  }
}
