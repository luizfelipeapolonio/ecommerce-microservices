package com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.ProductResponseDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.ProductAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.CreateProductUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.UploadFile;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.core.domain.Product;

import java.math.BigDecimal;
import java.util.Optional;

public class CreateProductUseCaseImpl implements CreateProductUseCase {
  private final ProductGateway productGateway;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
  private final GetBrandByIdUseCase getBrandByIdUseCase;
  private final GetModelByIdUseCase getModelByIdUseCase;

  public CreateProductUseCaseImpl(ProductGateway productGateway,
                                  GetCategoryByIdUseCase getCategoryByIdUseCase,
                                  GetBrandByIdUseCase  getBrandByIdUseCase,
                                  GetModelByIdUseCase getModelByIdUseCase) {
    this.productGateway = productGateway;
    this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    this.getBrandByIdUseCase = getBrandByIdUseCase;
    this.getModelByIdUseCase = getModelByIdUseCase;
  }

  @Override
  public ProductResponseDTO execute(CreateProductDomainDTO productDTO, UploadFile[] files) {
    Optional<Product> existingProduct = this.productGateway.findProductByName(productDTO.name());
    if(existingProduct.isPresent()) {
      throw new ProductAlreadyExistsException(productDTO.name());
    }

    Category category = this.getCategoryByIdUseCase.execute(productDTO.categoryId());
    Brand brand = this.getBrandByIdUseCase.execute(productDTO.brandId());
    Model model = this.getModelByIdUseCase.execute(productDTO.modelId());

    Product product = Product.builder()
      .name(productDTO.name())
      .description(productDTO.description())
      .unitPrice(new BigDecimal(productDTO.unitPrice()))
      .quantity(productDTO.quantity())
      .category(category)
      .brand(brand)
      .model(model)
      .build();

    return this.productGateway.createProduct(product, files);
  }
}
