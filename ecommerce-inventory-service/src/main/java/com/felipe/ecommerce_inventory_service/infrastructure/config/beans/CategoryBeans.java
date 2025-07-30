package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.DeleteCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetAllCategoriesUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.UpdateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.CreateCategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.CreateSubcategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.DeleteCategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.GetAllCategoriesUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.GetCategoryByIdUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.impl.category.UpdateCategoryUseCaseImpl;
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

  @Bean
  public DeleteCategoryUseCase deleteCategoryUseCase() {
    return new DeleteCategoryUseCaseImpl(categoryGateway);
  }
}
