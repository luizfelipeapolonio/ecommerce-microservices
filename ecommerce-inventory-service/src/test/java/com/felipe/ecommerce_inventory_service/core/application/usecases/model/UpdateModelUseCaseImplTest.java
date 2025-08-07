package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ModelAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.UpdateModelUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
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
public class UpdateModelUseCaseImplTest {

  @Mock
  private ModelGateway modelGateway;

  private UpdateModelUseCaseImpl updateModelUseCase;
  private List<Model> modelsDomain;

  @BeforeEach
  void setUp() {
    this.updateModelUseCase = new UpdateModelUseCaseImpl(this.modelGateway);
    this.modelsDomain = new DataMock().getModelsDomain();
  }

  @Test
  @DisplayName("updateModelSuccess - Should successfully update a model and return it")
  void updateModelSuccess() {
    Model model = this.modelsDomain.getFirst();
    final String updatedName = "updatedName";
    final String updatedDescription = "updatedDescription";

    when(this.modelGateway.findModelById(model.getId())).thenReturn(Optional.of(model));
    when(this.modelGateway.findModelByName(updatedName)).thenReturn(Optional.empty());
    when(this.modelGateway.updateModel(model, updatedName, updatedDescription)).thenReturn(model);

    Model updatedModel = this.updateModelUseCase.execute(model.getId(), updatedName, updatedDescription);

    assertThat(updatedModel.getId()).isEqualTo(model.getId());
    assertThat(updatedModel.getName()).isEqualTo(model.getName());
    assertThat(updatedModel.getDescription()).isEqualTo(model.getDescription());
    assertThat(updatedModel.getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(updatedModel.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    assertThat(updatedModel.getBrand()).isEqualTo(model.getBrand());

    verify(this.modelGateway, times(1)).findModelById(model.getId());
    verify(this.modelGateway, times(1)).findModelByName(updatedName);
    verify(this.modelGateway, times(1)).updateModel(model, updatedName, updatedDescription);
  }

  @Test
  @DisplayName("updateModelFailsByModelNotFound - Should throw a DataNotFoundException if the model is not found")
  void updateModelFailsByModelNotFound() {
    final Long modelId = 1L;

    when(this.modelGateway.findModelById(modelId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updateModelUseCase.execute(modelId, "anything", "anything"));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Modelo de id '%s' não encontrado", modelId);

    verify(this.modelGateway, times(1)).findModelById(modelId);
    verify(this.modelGateway, never()).findModelByName(anyString());
    verify(this.modelGateway, never()).updateModel(any(Model.class), anyString(), anyString());
  }

  @Test
  @DisplayName("updateModelFailsByModelAlreadyExists - Should throw a ModelAlreadyExistsException if the given model name already exists")
  void updateModelFailsByModelAlreadyExists() {
    Model model = this.modelsDomain.getFirst();
    final String updatedName = "updatedName";

    when(this.modelGateway.findModelById(model.getId())).thenReturn(Optional.of(model));
    when(this.modelGateway.findModelByName(updatedName)).thenReturn(Optional.of(model));

    Exception thrown = catchException(() -> this.updateModelUseCase.execute(model.getId(), updatedName, "anything"));

    assertThat(thrown)
      .isExactlyInstanceOf(ModelAlreadyExistsException.class)
      .hasMessage("O modelo '%s' já existe", updatedName);

    verify(this.modelGateway, times(1)).findModelById(model.getId());
    verify(this.modelGateway, times(1)).findModelByName(updatedName);
    verify(this.modelGateway, never()).updateModel(any(Model.class), anyString(), anyString());
  }
}