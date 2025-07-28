package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.GetAllCategoriesUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.UpdateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.CreateCategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.CreateSubcategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.GetAllCategoriesUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.GetCategoryByIdUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.UpdateCategoryUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryBeans {
  private final CategoryGateway categoryGateway;

  public CategoryBeans(CategoryGateway categoryGateway) {
    this.categoryGateway = categoryGateway;
  }

  @Bean
  public CreateCategoryUseCase createCategoryUseCase() {
    return new CreateCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public CreateSubcategoryUseCase createSubcategoryUseCase() {
    return new CreateSubcategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public UpdateCategoryUseCase updateCategoryUseCase() {
    return new UpdateCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public GetCategoryByIdUseCase getCategoryByIdUseCase() {
    return new GetCategoryByIdUseCaseImpl(categoryGateway);
  }

  @Bean
  public GetAllCategoriesUseCase getAllCategoriesUseCase() {
    return new GetAllCategoriesUseCaseImpl(categoryGateway);
  }
}
