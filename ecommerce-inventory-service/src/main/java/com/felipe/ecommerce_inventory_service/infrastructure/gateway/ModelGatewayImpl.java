package com.felipe.ecommerce_inventory_service.infrastructure.gateway;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.mappers.ModelEntityMapper;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ModelRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ModelGatewayImpl implements ModelGateway {
  private final ModelRepository modelRepository;
  private final ModelEntityMapper modelEntityMapper;

  public ModelGatewayImpl(ModelRepository modelRepository, ModelEntityMapper modelEntityMapper) {
    this.modelRepository = modelRepository;
    this.modelEntityMapper = modelEntityMapper;
  }

  @Override
  public Model createModel(Model model) {
    ModelEntity modelEntity = ModelEntity.mutate(this.modelEntityMapper.toEntity(model))
      .id(null)
      .build();
    return this.modelEntityMapper.toDomain(this.modelRepository.save(modelEntity));
  }

  @Override
  public Optional<Model> findModelByName(String name) {
    return this.modelRepository.findByName(name).map(this.modelEntityMapper::toDomain);
  }

  @Override
  public Optional<Model> findModelById(Long id) {
    return this.modelRepository.findById(id).map(this.modelEntityMapper::toDomain);
  }

  @Override
  public List<Model> findAllModelsByBrandId(Long brandId) {
    return this.modelRepository.findAllByBrandId(brandId)
      .stream()
      .map(this.modelEntityMapper::toDomain)
      .toList();
  }
}
