package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetAllModelsOfBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.UpdateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.CreateModelUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.GetAllModelsOfBrandUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.GetModelByIdUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.UpdateModelUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelBeans {
  private final ModelGateway modelGateway;
  private final GetBrandByIdUseCase getBrandByIdUseCase;

  public ModelBeans(ModelGateway modelGateway, GetBrandByIdUseCase getBrandByIdUseCase) {
    this.modelGateway = modelGateway;
    this.getBrandByIdUseCase = getBrandByIdUseCase;
  }

  @Bean
  public CreateModelUseCase createModelUseCase() {
    return new CreateModelUseCaseImpl(modelGateway, getBrandByIdUseCase);
  }

  @Bean
  public GetModelByIdUseCase getModelByIdUseCase() {
    return new GetModelByIdUseCaseImpl(modelGateway);
  }

  @Bean
  public GetAllModelsOfBrandUseCase getAllModelsOfBrandUseCase() {
    return new GetAllModelsOfBrandUseCaseImpl(modelGateway, getBrandByIdUseCase);
  }

  @Bean
  public UpdateModelUseCase updateModelUseCase() {
    return new UpdateModelUseCaseImpl(modelGateway);
  }
}
