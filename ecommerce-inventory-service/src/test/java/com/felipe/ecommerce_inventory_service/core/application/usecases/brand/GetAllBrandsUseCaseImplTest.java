package com.felipe.ecommerce_inventory_service.core.application.usecases.brand;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.GetAllBrandsUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetAllBrandsUseCaseImplTest {

  @Mock
  private BrandGateway brandGateway;

  private GetAllBrandsUseCaseImpl getAllBrandsUseCase;
  private List<Brand> brandsDomain;

  @BeforeEach
  void setUp() {
    this.getAllBrandsUseCase = new GetAllBrandsUseCaseImpl(this.brandGateway);
    this.brandsDomain = new DataMock().getBrandsDomain();
  }

  @Test
  @DisplayName("getAllBrandsReturnsFilledList - Should successfully find all brands and return it")
  void getAllBrandsReturnsFilledList() {
    when(this.brandGateway.getAllBrands()).thenReturn(this.brandsDomain);

    List<Brand> brands = this.getAllBrandsUseCase.execute();

    assertThat(brands.size()).isEqualTo(this.brandsDomain.size());
    assertThat(brands.stream().map(Brand::getId).toList())
      .containsExactlyInAnyOrderElementsOf(this.brandsDomain.stream().map(Brand::getId).toList());

    verify(this.brandGateway, times(1)).getAllBrands();
  }
}
