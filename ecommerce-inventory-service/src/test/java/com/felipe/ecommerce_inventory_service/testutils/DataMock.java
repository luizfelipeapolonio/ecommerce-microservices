package com.felipe.ecommerce_inventory_service.testutils;

import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataMock {
  private final List<Category> categoriesDomain = new ArrayList<>();
  private final List<CategoryEntity> categoriesEntity = new ArrayList<>();
  private final List<Brand> brandsDomain = new ArrayList<>();
  private final List<BrandEntity> brandsEntity = new ArrayList<>();
  private final List<Model> modelsDomain = new ArrayList<>();
  private final List<ModelEntity> modelsEntity = new ArrayList<>();

  public DataMock() {
    this.createCategoriesDomainMock();
    this.createCategoriesEntityMock();
    this.createBrandsDomainMock();
    this.createBrandsEntityMock();
    this.createModelsDomain();
    this.createModelsEntity();
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


    this.categoriesDomain.add(category1);
    this.categoriesDomain.add(category2);
    this.categoriesDomain.add(category3);
    this.categoriesDomain.add(category4);
    this.categoriesDomain.add(category5);
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

    this.categoriesEntity.add(category1);
    this.categoriesEntity.add(category2);
    this.categoriesEntity.add(category3);
    this.categoriesEntity.add(category4);
    this.categoriesEntity.add(category5);
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

  private void createModelsDomain() {
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

    this.modelsDomain.add(model1);
    this.modelsDomain.add(model2);
  }

  private void createModelsEntity() {
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

    this.modelsEntity.add(model1);
    this.modelsEntity.add(model2);
  }
}
