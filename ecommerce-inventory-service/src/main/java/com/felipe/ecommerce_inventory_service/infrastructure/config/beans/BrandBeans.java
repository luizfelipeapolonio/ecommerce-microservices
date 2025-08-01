package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.CreateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.CreateBrandUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BrandBeans {
  private final BrandGateway brandGateway;

  public BrandBeans(BrandGateway brandGateway) {
    this.brandGateway = brandGateway;
  }

  @Bean
  public CreateBrandUseCase createBrandUseCase() {
    return new CreateBrandUseCaseImpl(brandGateway);
  }
}
