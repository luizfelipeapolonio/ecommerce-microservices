package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.GetAllModelsOfBrandUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
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
public class GetAllModelsOfBrandUseCaseImplTest {

  @Mock
  private ModelGateway modelGateway;

  @Mock
  private GetBrandByIdUseCase getBrandByIdUseCase;

  private GetAllModelsOfBrandUseCaseImpl getAllModelsOfBrandUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getAllModelsOfBrandUseCase = new GetAllModelsOfBrandUseCaseImpl(this.modelGateway, this.getBrandByIdUseCase);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getAllModelsOfBrandSuccess - Should successfully get all brand models given the brand id")
  void getAllModelsOfBrandSuccess() {
    final Long brandId = 1L;
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    List<Model> models = List.of(this.dataMock.getModelsDomain().get(0), this.dataMock.getModelsDomain().get(2),
                                 this.dataMock.getModelsDomain().get(3));

    when(this.getBrandByIdUseCase.execute(brandId)).thenReturn(brand);
    when(this.modelGateway.findAllModelsByBrandId(brand.getId())).thenReturn(models);

    List<Model> allModels = this.getAllModelsOfBrandUseCase.execute(brandId);

    assertThat(allModels.size()).isEqualTo(models.size());
    assertThat(allModels.stream().map(Model::getId).toList())
      .containsExactlyInAnyOrderElementsOf(models.stream().map(Model::getId).toList());

    verify(this.getBrandByIdUseCase, times(1)).execute(brandId);
    verify(this.modelGateway, times(1)).findAllModelsByBrandId(brand.getId());
  }
}
