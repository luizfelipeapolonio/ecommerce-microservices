package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ModelEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ModelRepository;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class ModelGatewayImplTest {

  @Mock
  private ModelRepository modelRepository;

  @Mock
  private ModelEntityMapper modelEntityMapper;

  @InjectMocks
  private ModelGatewayImpl modelGateway;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createModelSuccess - Should successfully create a model and return it")
  void createModelSuccess() {
    Model model = this.dataMock.getModelsDomain().getFirst();
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();
    ArgumentCaptor<ModelEntity> entityCaptor = ArgumentCaptor.forClass(ModelEntity.class);

    when(this.modelEntityMapper.toEntity(model)).thenReturn(modelEntity);
    when(this.modelRepository.save(entityCaptor.capture())).thenReturn(modelEntity);
    when(this.modelEntityMapper.toDomain(modelEntity)).thenReturn(model);

    Model createdModel = this.modelGateway.createModel(model);

    // argument captor assertions
    assertThat(entityCaptor.getValue().getName()).isEqualTo(model.getName());
    assertThat(entityCaptor.getValue().getDescription()).isEqualTo(model.getDescription());
    assertThat(entityCaptor.getValue().getBrand().getId()).isEqualTo(model.getBrand().getId());
    // created model assertions
    assertThat(createdModel.getId()).isEqualTo(model.getId());
    assertThat(createdModel.getName()).isEqualTo(model.getName());
    assertThat(createdModel.getDescription()).isEqualTo(model.getDescription());
    assertThat(createdModel.getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(createdModel.getUpdatedAt()).isEqualTo(model.getUpdatedAt());
    assertThat(createdModel.getBrand()).isEqualTo(model.getBrand());

    verify(this.modelEntityMapper, times(1)).toEntity(model);
    verify(this.modelRepository, times(1)).save(any(ModelEntity.class));
    verify(this.modelEntityMapper, times(1)).toDomain(modelEntity);
  }

  @Test
  @DisplayName("findModelByNameSuccess - Should successfully find a model by name and return an Optional of Model")
  void findModelByNameSuccess() {
    Model model = this.dataMock.getModelsDomain().getFirst();
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();

    when(this.modelRepository.findByName(model.getName())).thenReturn(Optional.of(modelEntity));
    when(this.modelEntityMapper.toDomain(modelEntity)).thenReturn(model);

    Optional<Model> foundModel = this.modelGateway.findModelByName(model.getName());

    assertThat(foundModel).isPresent();
    assertThat(foundModel.get().getId()).isEqualTo(model.getId());
    assertThat(foundModel.get().getName()).isEqualTo(model.getName());
    assertThat(foundModel.get().getDescription()).isEqualTo(model.getDescription());
    assertThat(foundModel.get().getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(foundModel.get().getUpdatedAt()).isEqualTo(model.getUpdatedAt());

    verify(this.modelRepository, times(1)).findByName(model.getName());
    verify(this.modelEntityMapper, times(1)).toDomain(modelEntity);
  }

  @Test
  @DisplayName("findModelByIdSuccess - Should successfully find a model by id and return an Optional of Model")
  void findModelByIdSuccess() {
    Model model = this.dataMock.getModelsDomain().getFirst();
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();

    when(this.modelRepository.findById(model.getId())).thenReturn(Optional.of(modelEntity));
    when(this.modelEntityMapper.toDomain(modelEntity)).thenReturn(model);

    Optional<Model> foundModel = this.modelGateway.findModelById(model.getId());

    assertThat(foundModel).isPresent();
    assertThat(foundModel.get().getId()).isEqualTo(model.getId());
    assertThat(foundModel.get().getName()).isEqualTo(model.getName());
    assertThat(foundModel.get().getDescription()).isEqualTo(model.getDescription());
    assertThat(foundModel.get().getCreatedAt()).isEqualTo(model.getCreatedAt());
    assertThat(foundModel.get().getUpdatedAt()).isEqualTo(model.getUpdatedAt());

    verify(this.modelRepository, times(1)).findById(model.getId());
    verify(this.modelEntityMapper, times(1)).toDomain(modelEntity);
  }

  @Test
  @DisplayName("updateModelSuccess - Should successfully update a model and return it")
  void updateModelSuccess() {
    Model modelDomain = this.dataMock.getModelsDomain().getFirst();
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();
    final String updatedName = "updatedName";
    final String updatedDescription = "updatedDescription";
    ArgumentCaptor<ModelEntity> entityCaptor = ArgumentCaptor.forClass(ModelEntity.class);

    when(this.modelEntityMapper.toEntity(modelDomain)).thenReturn(modelEntity);
    when(this.modelRepository.save(entityCaptor.capture())).thenReturn(modelEntity);
    when(this.modelEntityMapper.toDomain(modelEntity)).thenReturn(modelDomain);

    Model updatedModel = this.modelGateway.updateModel(modelDomain, updatedName, updatedDescription);

    // argument captor assertions
    assertThat(entityCaptor.getValue().getName()).isEqualTo(updatedName);
    assertThat(entityCaptor.getValue().getDescription()).isEqualTo(updatedDescription);
    // updated model assertions
    assertThat(updatedModel.getId()).isEqualTo(modelDomain.getId());
    assertThat(updatedModel.getName()).isEqualTo(modelDomain.getName());
    assertThat(updatedModel.getDescription()).isEqualTo(modelDomain.getDescription());
    assertThat(updatedModel.getCreatedAt()).isEqualTo(modelDomain.getCreatedAt());
    assertThat(updatedModel.getUpdatedAt()).isEqualTo(modelDomain.getUpdatedAt());
    assertThat(updatedModel.getBrand()).isEqualTo(modelDomain.getBrand());

    verify(this.modelEntityMapper, times(1)).toEntity(modelDomain);
    verify(this.modelRepository, times(1)).save(any(ModelEntity.class));
    verify(this.modelEntityMapper, times(1)).toDomain(modelEntity);
  }

  @Test
  @DisplayName("deleteModelSuccess - Should successfully delete a model and return the deleted model")
  void deleteModelSuccess() {
    Model modelDomain = this.dataMock.getModelsDomain().getFirst();
    ModelEntity modelEntity = this.dataMock.getModelsEntity().getFirst();

    when(this.modelEntityMapper.toEntity(modelDomain)).thenReturn(modelEntity);
    doNothing().when(this.modelRepository).delete(modelEntity);

    Model deletedModel = this.modelGateway.deleteModel(modelDomain);

    assertThat(deletedModel.getId()).isEqualTo(modelDomain.getId());
    assertThat(deletedModel.getName()).isEqualTo(modelDomain.getName());
    assertThat(deletedModel.getDescription()).isEqualTo(modelDomain.getDescription());
    assertThat(deletedModel.getCreatedAt()).isEqualTo(modelDomain.getCreatedAt());
    assertThat(deletedModel.getUpdatedAt()).isEqualTo(modelDomain.getUpdatedAt());
    assertThat(deletedModel.getBrand()).isEqualTo(modelDomain.getBrand());

    verify(this.modelEntityMapper, times(1)).toEntity(modelDomain);
    verify(this.modelRepository, times(1)).delete(modelEntity);
  }
}