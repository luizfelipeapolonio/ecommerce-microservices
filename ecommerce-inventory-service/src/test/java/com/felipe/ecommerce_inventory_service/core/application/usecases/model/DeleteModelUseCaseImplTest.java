package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.DeleteModelUseCaseImpl;
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

@ExtendWith(MockitoExtension.class)
public class DeleteModelUseCaseImplTest {

  @Mock
  private ModelGateway modelGateway;

  private DeleteModelUseCaseImpl deleteModelUseCase;
  private List<Model> modelsDomain;

  @BeforeEach
  void setUp() {
    this.deleteModelUseCase = new DeleteModelUseCaseImpl(this.modelGateway);
    this.modelsDomain = new DataMock().getModelsDomain();
  }

  @Test
  @DisplayName("deleteModelSuccess - Should successfully delete a model and return the deleted model")
  void deleteModelSuccess() {
    Model model = this.modelsDomain.getFirst();

    when(this.modelGateway.findModelById(model.getId())).thenReturn(Optional.of(model));
    when(this.modelGateway.deleteModel(model)).thenReturn(model);

    Model deletedModel = this.deleteModelUseCase.execute(model.getId());

    assertThat(deletedModel.getId()).isEqualTo(model.getId());
    assertThat(deletedModel.getName()).isEqualTo(model.getName());
    assertThat(deletedModel.getDescription()).isEqualTo(model.getDescription());
    assertThat(deletedModel.getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(deletedModel.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    assertThat(deletedModel.getBrand()).isEqualTo(model.getBrand());

    verify(this.modelGateway, times(1)).findModelById(model.getId());
    verify(this.modelGateway, times(1)).deleteModel(model);
  }

  @Test
  @DisplayName("deleteModelFailsByModelNotFound - Should throw a DataNotFoundException if the model is not found")
  void deleteModelFailsByModelNotFound() {
    final Long modelId = 1L;

    when(this.modelGateway.findModelById(modelId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deleteModelUseCase.execute(modelId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Modelo de id '%s' não encontrado", modelId);

    verify(this.modelGateway, times(1)).findModelById(modelId);
    verify(this.modelGateway, never()).deleteModel(any(Model.class));
  }
}