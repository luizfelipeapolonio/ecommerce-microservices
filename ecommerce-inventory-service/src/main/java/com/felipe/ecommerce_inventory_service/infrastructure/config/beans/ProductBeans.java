package com.felipe.ecommerce_inventory_service.infrastructure.config.beans;

import com.felipe.ecommerce_inventory_service.core.application.gateway.BrandGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.CategoryGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ModelGateway;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.AddProductInStockUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CheckIfProductIsInStockUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CreateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.DeleteProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetAllProductsUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsByModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.GetProductsUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.RemoveProductFromStockUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UpdateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.AddProductInStockUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.CheckIfProductIsInStockUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.CreateProductUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.DeleteProductUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetAllProductsUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductByIdUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsByBrandUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsByCategoryUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsByModelUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.GetProductsUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.RemoveProductFromStockUseCaseImpl;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.UpdateProductUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductBeans {
  private final ProductGateway productGateway;
  private final CategoryGateway categoryGateway;
  private final BrandGateway brandGateway;
  private final ModelGateway modelGateway;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
  private final GetBrandByIdUseCase getBrandByIdUseCase;
  private final GetModelByIdUseCase getModelByIdUseCase;

  public ProductBeans(ProductGateway productGateway,
                      CategoryGateway categoryGateway,
                      BrandGateway brandGateway,
                      ModelGateway modelGateway,
                      GetCategoryByIdUseCase getCategoryByIdUseCase,
                      GetBrandByIdUseCase getBrandByIdUseCase,
                      GetModelByIdUseCase getModelByIdUseCase) {
    this.productGateway = productGateway;
    this.categoryGateway = categoryGateway;
    this.brandGateway = brandGateway;
    this.modelGateway = modelGateway;
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

  @Bean
  public GetProductsUseCase getProductsUseCase() {
    return new GetProductsUseCaseImpl(productGateway);
  }

  @Bean
  public GetAllProductsUseCase getAllProductsUseCase() {
    return new GetAllProductsUseCaseImpl(productGateway);
  }

  @Bean
  public GetProductByIdUseCase getProductByIdUseCase() {
    return new GetProductByIdUseCaseImpl(productGateway);
  }

  @Bean
  public GetProductsByCategoryUseCase getProductsByCategoryUseCase() {
    return new GetProductsByCategoryUseCaseImpl(productGateway, categoryGateway);
  }

  @Bean
  public GetProductsByBrandUseCase getProductsByBrandUseCase() {
    return new GetProductsByBrandUseCaseImpl(productGateway, brandGateway);
  }

  @Bean
  public GetProductsByModelUseCase getProductsByModelUseCase() {
    return new GetProductsByModelUseCaseImpl(productGateway, modelGateway);
  }

  @Bean
  public DeleteProductUseCase deleteProductUseCase() {
    return new DeleteProductUseCaseImpl(productGateway);
  }

  @Bean
  public CheckIfProductIsInStockUseCase checkIfProductIsInStock() {
    return new CheckIfProductIsInStockUseCaseImpl(productGateway);
  }

  @Bean
  public AddProductInStockUseCase addProductInStockUseCase() {
    return new AddProductInStockUseCaseImpl(productGateway);
  }

  @Bean
  public RemoveProductFromStockUseCase removeProductFromStockUseCase() {
    return new RemoveProductFromStockUseCaseImpl(productGateway);
  }
}
