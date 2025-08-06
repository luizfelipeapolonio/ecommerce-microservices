package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.ModelAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.CreateModelUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class CreateModelUseCaseImplTest {

  @Mock
  private ModelGateway modelGateway;

  @Mock
  private GetBrandByIdUseCase getBrandByIdUseCase;

  private CreateModelUseCaseImpl createModelUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.createModelUseCase = new CreateModelUseCaseImpl(this.modelGateway, this.getBrandByIdUseCase);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createModelSuccess - Should successfully create a model and return it")
  void createModelSuccess() {
    Model model = this.dataMock.getModelsDomain().getFirst();
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    final String modelName = "g pro";
    final String modelDescription = "A great model";
    final Long brandId = 1L;
    ArgumentCaptor<Model> modelCaptor = ArgumentCaptor.forClass(Model.class);

    when(this.getBrandByIdUseCase.execute(brandId)).thenReturn(brand);
    when(this.modelGateway.findModelByName(modelName)).thenReturn(Optional.empty());
    when(this.modelGateway.createModel(modelCaptor.capture())).thenReturn(model);

    Model createdModel = this.createModelUseCase.execute(modelName, modelDescription, brandId);

    // argument captor assertions
    assertThat(modelCaptor.getValue().getName()).isEqualTo(modelName);
    assertThat(modelCaptor.getValue().getDescription()).isEqualTo(modelDescription);
    assertThat(modelCaptor.getValue().getBrand().getId()).isEqualTo(brandId);
    // created model assertions
    assertThat(createdModel.getId()).isEqualTo(model.getId());
    assertThat(createdModel.getName()).isEqualTo(model.getName());
    assertThat(createdModel.getDescription()).isEqualTo(model.getDescription());
    assertThat(createdModel.getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(createdModel.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    assertThat(createdModel.getBrand()).isEqualTo(model.getBrand());

    verify(this.getBrandByIdUseCase, times(1)).execute(brandId);
    verify(this.modelGateway, times(1)).findModelByName(modelName);
    verify(this.modelGateway, times(1)).createModel(any(Model.class));
  }

  @Test
  @DisplayName("createModelFailsByModelAlreadyExists - Should throw a ModelAlreadyExistsException if model already exists")
  void createModelFailsByModelAlreadyExists() {
    Model model = this.dataMock.getModelsDomain().getFirst();
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    final Long brandId = 1L;
    final String modelName = "g pro";

    when(this.getBrandByIdUseCase.execute(brandId)).thenReturn(brand);
    when(this.modelGateway.findModelByName(modelName)).thenReturn(Optional.of(model));

    Exception thrown = catchException(() -> this.createModelUseCase.execute(modelName, "anything", brandId));

    assertThat(thrown)
      .isExactlyInstanceOf(ModelAlreadyExistsException.class)
      .hasMessage("O modelo '%s' já existe", modelName);

    verify(this.getBrandByIdUseCase, times(1)).execute(brandId);
    verify(this.modelGateway, times(1)).findModelByName(modelName);
    verify(this.modelGateway, never()).createModel(any(Model.class));
  }
}
