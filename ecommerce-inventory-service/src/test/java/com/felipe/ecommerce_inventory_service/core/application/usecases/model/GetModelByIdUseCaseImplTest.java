package com.felipe.ecommerce_inventory_service.core.application.usecases.model;

import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.GetModelByIdUseCaseImpl;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class GetModelByIdUseCaseImplTest {

  @Mock
  private ModelGateway modelGateway;

  private GetModelByIdUseCaseImpl getModelByIdUseCase;
  private List<Model> modelsDomain;

  @BeforeEach
  void setUp() {
    this.getModelByIdUseCase = new GetModelByIdUseCaseImpl(this.modelGateway);
    this.modelsDomain = new DataMock().getModelsDomain();
  }

  @Test
  @DisplayName("getModelByIdSuccess - Should successfully find a model by id and return it")
  void getModelByIdSuccess() {
    Model model = this.modelsDomain.getFirst();

    when(this.modelGateway.findModelById(model.getId())).thenReturn(Optional.of(model));

    Model foundModel = this.getModelByIdUseCase.execute(model.getId());

    assertThat(foundModel.getId()).isEqualTo(model.getId());
    assertThat(foundModel.getName()).isEqualTo(model.getName());
    assertThat(foundModel.getDescription()).isEqualTo(model.getDescription());
    assertThat(foundModel.getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(foundModel.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    assertThat(foundModel.getBrand()).isEqualTo(model.getBrand());

    verify(this.modelGateway, times(1)).findModelById(model.getId());
  }

  @Test
  @DisplayName("getModelByIdUseCaseFailsByModelNotFound - Should throw a DataNotFoundException if model is not found")
  void getModelByIdUseCaseFailsByModelNotFound() {
    final Long modelId = 1L;

    when(this.modelGateway.findModelById(modelId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getModelByIdUseCase.execute(modelId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Modelo de id '%s' não encontrado", modelId);

    verify(this.modelGateway, times(1)).findModelById(modelId);
  }
}