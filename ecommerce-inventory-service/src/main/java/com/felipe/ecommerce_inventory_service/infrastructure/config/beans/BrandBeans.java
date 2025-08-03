package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.CreateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetAllBrandsUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.UpdateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.CreateBrandUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.GetAllBrandsUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.GetBrandByIdUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.impl.UpdateBrandUseCaseImpl;
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

  @Bean
  public GetBrandByIdUseCase getBrandByIdUseCase() {
    return new GetBrandByIdUseCaseImpl(brandGateway);
  }

  @Bean
  public GetAllBrandsUseCase getAllBrandsUseCase() {
    return new GetAllBrandsUseCaseImpl(brandGateway);
  }

  @Bean
  public UpdateBrandUseCase updateBrandUseCase() {
    return new UpdateBrandUseCaseImpl(brandGateway);
  }
}
