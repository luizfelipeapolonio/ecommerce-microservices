package com.felipe.ecommerce_inventory_service.testutils;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.core.domain.Product;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataMock {
  private final List<Category> categoriesDomain = new ArrayList<>();
  private final List<CategoryEntity> categoriesEntity = new ArrayList<>();
  private final List<Brand> brandsDomain = new ArrayList<>();
  private final List<BrandEntity> brandsEntity = new ArrayList<>();
  private final List<Model> modelsDomain = new ArrayList<>();
  private final List<ModelEntity> modelsEntity = new ArrayList<>();
  private final List<Product> productsDomain = new ArrayList<>();
  private final List<ProductEntity> productsEntity = new ArrayList<>();

  public DataMock() {
    this.createCategoriesDomainMock();
    this.createCategoriesEntityMock();
    this.createBrandsDomainMock();
    this.createBrandsEntityMock();
    this.createModelsDomainMock();
    this.createModelsEntityMock();
    this.createProductsDomainMock();
    this.createProductsEntityMock();
  }

  public List<Category> getCategoriesDomain() {
    return this.categoriesDomain;
  }

  public List<CategoryEntity> getCategoriesEntity() {
    return this.categoriesEntity;
  }

  public List<Brand> getBrandsDomain() {
    return this.brandsDomain;
  }

  public List<BrandEntity> getBrandsEntity() {
    return this.brandsEntity;
  }

  public List<Model> getModelsDomain() {
    return this.modelsDomain;
  }

  public List<ModelEntity> getModelsEntity() {
    return this.modelsEntity;
  }

  public List<Product> getProductsDomain() {
    return this.productsDomain;
  }

  public List<ProductEntity> getProductsEntity() {
    return this.productsEntity;
  }

  private void createCategoriesDomainMock() {
    Category category1 = Category.builder()
      .id(1L)
      .name("hardware")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Category category2 = Category.builder()
      .id(2L)
      .name("motherboards")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .parentCategory(category1)
      .build();

    Category category3 = Category.builder()
      .id(3L)
      .name("cpus")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .parentCategory(category1)
      .build();

    Category category4 = Category.builder()
      .id(4L)
      .name("peripherals")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Category category5 = Category.builder()
      .id(5L)
      .name("mouse")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .parentCategory(category4)
      .build();

    Category category6 = Category.builder()
      .id(6L)
      .name("ram")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .parentCategory(category1)
      .build();


    this.categoriesDomain.add(category1);
    this.categoriesDomain.add(category2);
    this.categoriesDomain.add(category3);
    this.categoriesDomain.add(category4);
    this.categoriesDomain.add(category5);
    this.categoriesDomain.add(category6);
  }

  private void createCategoriesEntityMock() {
    CategoryEntity category1 = new CategoryEntity();
    category1.setId(1L);
    category1.setName("hardware");
    category1.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category1.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));

    CategoryEntity category2 = new CategoryEntity();
    category2.setId(2L);
    category2.setName("motherboards");
    category2.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category2.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category2.setParentCategory(category1);

    CategoryEntity category3 = new CategoryEntity();
    category3.setId(3L);
    category3.setName("cpus");
    category3.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category3.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category3.setParentCategory(category1);

    CategoryEntity category4 = new CategoryEntity();
    category4.setId(4L);
    category4.setName("peripherals");
    category4.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category4.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));

    CategoryEntity category5 = new CategoryEntity();
    category5.setId(5L);
    category5.setName("mouse");
    category5.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category5.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category5.setParentCategory(category4);

    CategoryEntity category6 = new CategoryEntity();
    category6.setId(6L);
    category6.setName("ram");
    category6.setCreatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category6.setUpdatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"));
    category6.setParentCategory(category1);

    this.categoriesEntity.add(category1);
    this.categoriesEntity.add(category2);
    this.categoriesEntity.add(category3);
    this.categoriesEntity.add(category4);
    this.categoriesEntity.add(category5);
    this.categoriesEntity.add(category6);
  }

  private void createBrandsDomainMock() {
    Brand brand1 = Brand.builder()
      .id(1L)
      .name("logitech")
      .description("A great gamer brand")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();
    Brand brand2 = Brand.builder()
      .id(2L)
      .name("nvidia")
      .description("A great brand")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();
    Brand brand3 = Brand.builder()
      .id(3L)
      .name("corsair")
      .description("A great brand")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    this.brandsDomain.add(brand1);
    this.brandsDomain.add(brand2);
    this.brandsDomain.add(brand3);
  }

  private void createBrandsEntityMock() {
    BrandEntity brand1 = BrandEntity.builder()
      .id(1L)
      .name("logitech")
      .description("A great gamer brand")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();
    BrandEntity brand2 = BrandEntity.builder()
      .id(2L)
      .name("nvidia")
      .description("A great brand")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();
    BrandEntity brand3 = BrandEntity.builder()
      .id(3L)
      .name("corsair")
      .description("A great brand")
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    this.brandsEntity.add(brand1);
    this.brandsEntity.add(brand2);
    this.brandsEntity.add(brand3);
  }

  private void createModelsDomainMock() {
    Model model1 = Model.builder()
      .id(1L)
      .name("g pro")
      .description("A great model")
      .brand(this.getBrandsDomain().get(0))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Model model2 = Model.builder()
      .id(2L)
      .name("geforce rtx")
      .description("A great model")
      .brand(this.getBrandsDomain().get(1))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Model model3 = Model.builder()
      .id(3L)
      .name("g502 x")
      .description("A great model")
      .brand(this.getBrandsDomain().get(0))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Model model4 = Model.builder()
      .id(4L)
      .name("pro x")
      .description("A great model")
      .brand(this.getBrandsDomain().get(0))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    Model model5 = Model.builder()
      .id(5L)
      .name("vengeance")
      .description("A great model")
      .brand(this.getBrandsDomain().get(2))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    this.modelsDomain.add(model1);
    this.modelsDomain.add(model2);
    this.modelsDomain.add(model3);
    this.modelsDomain.add(model4);
    this.modelsDomain.add(model5);
  }

  private void createModelsEntityMock() {
    ModelEntity model1 = ModelEntity.builder()
      .id(1L)
      .name("g pro")
      .description("A great model")
      .brand(this.getBrandsEntity().get(0))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    ModelEntity model2 = ModelEntity.builder()
      .id(2L)
      .name("geforce rtx")
      .description("A great model")
      .brand(this.getBrandsEntity().get(1))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    ModelEntity model3 = ModelEntity.builder()
      .id(3L)
      .name("g502 x")
      .description("A great model")
      .brand(this.getBrandsEntity().get(0))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    ModelEntity model4 = ModelEntity.builder()
      .id(4L)
      .name("pro x")
      .description("A great model")
      .brand(this.getBrandsEntity().get(0))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    ModelEntity model5 = ModelEntity.builder()
      .id(5L)
      .name("vengeance")
      .description("A great model")
      .brand(this.getBrandsEntity().get(2))
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .build();

    this.modelsEntity.add(model1);
    this.modelsEntity.add(model2);
    this.modelsEntity.add(model3);
    this.modelsEntity.add(model4);
    this.modelsEntity.add(model5);
  }

  private void createProductsDomainMock() {
    Product product1 = Product.builder()
      .id(UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"))
      .name("Mouse wireless Logitech G PRO")
      .description("A technical and descriptive text about the product")
      .unitPrice(new BigDecimal("120.00"))
      .quantity(50)
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .category(this.getCategoriesDomain().get(4))
      .brand(this.getBrandsDomain().get(0))
      .model(this.getModelsDomain().get(0))
      .build();

    Product product2 = Product.builder()
      .id(UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"))
      .name("Mouse wireless Logitech M280")
      .description("A technical and descriptive text about the product")
      .unitPrice(new BigDecimal("80.00"))
      .quantity(50)
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .category(this.getCategoriesDomain().get(4))
      .brand(this.getBrandsDomain().get(0))
      .model(this.getModelsDomain().get(0))
      .build();

    Product product3 = Product.builder()
      .id(UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"))
      .name("Corsair RAM memory 16GB 3200MHz")
      .description("A technical and descriptive text about the product")
      .unitPrice(new BigDecimal("200.00"))
      .quantity(50)
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .category(this.getCategoriesDomain().get(5))
      .brand(this.getBrandsDomain().get(2))
      .model(this.getModelsDomain().get(4))
      .build();

    this.productsDomain.add(product1);
    this.productsDomain.add(product2);
    this.productsDomain.add(product3);
  }

  private void createProductsEntityMock() {
    ProductEntity product1 = ProductEntity.builder()
      .id(UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"))
      .name("Mouse wireless Logitech G PRO")
      .description("A technical and descriptive text about the product")
      .unitPrice(new BigDecimal("120.00"))
      .quantity(50)
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .category(this.getCategoriesEntity().get(4))
      .brand(this.getBrandsEntity().get(0))
      .model(this.getModelsEntity().get(0))
      .build();

    ProductEntity product2 = ProductEntity.builder()
      .id(UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"))
      .name("Mouse wireless Logitech M280")
      .description("A technical and descriptive text about the product")
      .unitPrice(new BigDecimal("80.00"))
      .quantity(50)
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .category(this.getCategoriesEntity().get(4))
      .brand(this.getBrandsEntity().get(0))
      .model(this.getModelsEntity().get(0))
      .build();

    ProductEntity product3 = ProductEntity.builder()
      .id(UUID.fromString("54e210c9-8d3b-48fd-9c73-e8b7d5fe7503"))
      .name("Corsair RAM memory 16GB 3200MHz")
      .description("A technical and descriptive text about the product")
      .unitPrice(new BigDecimal("200.00"))
      .quantity(50)
      .createdAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .updatedAt(LocalDateTime.parse("2025-07-18T21:12:28.978228256"))
      .category(this.getCategoriesEntity().get(5))
      .brand(this.getBrandsEntity().get(2))
      .model(this.getModelsEntity().get(4))
      .build();

    this.productsEntity.add(product1);
    this.productsEntity.add(product2);
    this.productsEntity.add(product3);
  }
}
