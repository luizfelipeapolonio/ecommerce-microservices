package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.impl.CreateModelUseCaseImpl;
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
}
