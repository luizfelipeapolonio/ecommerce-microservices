package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CreateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UpdateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.CreateProductUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UpdateProductUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductBeans {
  private final ProductGateway productGateway;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
  private final GetBrandByIdUseCase getBrandByIdUseCase;
  private final GetModelByIdUseCase getModelByIdUseCase;

  public ProductBeans(ProductGateway productGateway,
                      GetCategoryByIdUseCase getCategoryByIdUseCase,
                      GetBrandByIdUseCase getBrandByIdUseCase,
                      GetModelByIdUseCase getModelByIdUseCase) {
    this.productGateway = productGateway;
    this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    this.getBrandByIdUseCase = getBrandByIdUseCase;
    this.getModelByIdUseCase = getModelByIdUseCase;
  }

  @Bean
  public CreateProductUseCase createProductUseCase() {
    return new CreateProductUseCaseImpl(productGateway, getCategoryByIdUseCase, getBrandByIdUseCase, getModelByIdUseCase);
  }

  @Bean
  public UpdateProductUseCase updateProductUseCase() {
    return new UpdateProductUseCaseImpl(productGateway);
  }
}
