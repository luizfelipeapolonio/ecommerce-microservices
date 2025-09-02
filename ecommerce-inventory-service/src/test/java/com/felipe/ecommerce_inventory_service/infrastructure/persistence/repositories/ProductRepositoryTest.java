package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.BrandEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.CategoryEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ModelEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "test")
public class ProductRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  ProductRepository productRepository;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("findByCategoryNameReturnsProducts - Should successfully find all products of the given category name and return a page of products")
  void findByCategoryNameReturnsProducts() {
    final List<CategoryEntity> categories = this.dataMock.getCategoriesEntity()
      .stream()
      .peek(category -> category.setId(null))
      .toList();
    final List<BrandEntity> brands = this.dataMock.getBrandsEntity()
      .stream()
      .map(brand -> BrandEntity.mutate(brand).id(null).build())
      .toList();

    final List<ModelEntity.Builder> modelsBuilder = this.dataMock.getModelsEntity()
      .stream()
      .map(model -> ModelEntity.mutate(model).id(null))
      .toList();
    // Setting model's brand with null ids
    final List<ModelEntity> models = List.of(
      modelsBuilder.get(0).brand(brands.get(0)).build(),
      modelsBuilder.get(1).brand(brands.get(1)).build(),
      modelsBuilder.get(2).brand(brands.get(0)).build(),
      modelsBuilder.get(3).brand(brands.get(0)).build(),
      modelsBuilder.get(4).brand(brands.get(2)).build()
    );

    final List<ProductEntity.Builder> productsBuilder = this.dataMock.getProductsEntity()
      .stream()
      .map(product -> ProductEntity.mutate(product).id(null))
      .toList();
    // Setting product entities with null ids
    final List<ProductEntity> products = List.of(
      productsBuilder.get(0).category(categories.get(4)).brand(brands.get(0)).model(models.get(0)).build(),
      productsBuilder.get(1).category(categories.get(4)).brand(brands.get(0)).model(models.get(2)).build(),
      productsBuilder.get(2).category(categories.get(5)).brand(brands.get(2)).model(models.get(4)).build()
    );

    //Persisting entities
    categories.forEach(this.entityManager::persist);
    brands.forEach(this.entityManager::persist);
    models.forEach(this.entityManager::persist);
    products.forEach(this.entityManager::persist);

    final String categoryName = "mouse";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByCategoryName(categoryName, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(2);
    assertThat(productsPage.getTotalElements()).isEqualTo(2);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(2);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> assertThat(product.getCategory().getName()).isEqualTo(categoryName));

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(products.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(products.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(products.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(products.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(products.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(products.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(products.get(0).getUpdatedAt());
    assertThat(productsPage.getContent().get(0).getBrand()).usingRecursiveComparison().isEqualTo(products.get(0).getBrand());
    assertThat(productsPage.getContent().get(0).getModel()).usingRecursiveComparison().isEqualTo(products.get(0).getModel());

    assertThat(productsPage.getContent().get(1).getId()).isEqualTo(products.get(1).getId());
    assertThat(productsPage.getContent().get(1).getName()).isEqualTo(products.get(1).getName());
    assertThat(productsPage.getContent().get(1).getDescription()).isEqualTo(products.get(1).getDescription());
    assertThat(productsPage.getContent().get(1).getUnitPrice().toString()).isEqualTo(products.get(1).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(1).getQuantity()).isEqualTo(products.get(1).getQuantity());
    assertThat(productsPage.getContent().get(1).getCreatedAt()).isEqualTo(products.get(1).getCreatedAt());
    assertThat(productsPage.getContent().get(1).getUpdatedAt()).isEqualTo(products.get(1).getUpdatedAt());
    assertThat(productsPage.getContent().get(1).getBrand()).usingRecursiveComparison().isEqualTo(products.get(1).getBrand());
    assertThat(productsPage.getContent().get(1).getModel()).usingRecursiveComparison().isEqualTo(products.get(1).getModel());
  }

  @Test
  @DisplayName("findByBrandNameReturnsProducts - Should successfully find all products of the given brand name and return a page of products")
  void findByBrandNameReturnsProducts() {
    final List<CategoryEntity> categories = this.dataMock.getCategoriesEntity()
      .stream()
      .peek(category -> category.setId(null))
      .toList();
    final List<BrandEntity> brands = this.dataMock.getBrandsEntity()
      .stream()
      .map(brand -> BrandEntity.mutate(brand).id(null).build())
      .toList();

    final List<ModelEntity.Builder> modelsBuilder = this.dataMock.getModelsEntity()
      .stream()
      .map(model -> ModelEntity.mutate(model).id(null))
      .toList();
    // Setting model's brand with null ids
    final List<ModelEntity> models = List.of(
      modelsBuilder.get(0).brand(brands.get(0)).build(),
      modelsBuilder.get(1).brand(brands.get(1)).build(),
      modelsBuilder.get(2).brand(brands.get(0)).build(),
      modelsBuilder.get(3).brand(brands.get(0)).build(),
      modelsBuilder.get(4).brand(brands.get(2)).build()
    );

    final List<ProductEntity.Builder> productsBuilder = this.dataMock.getProductsEntity()
      .stream()
      .map(product -> ProductEntity.mutate(product).id(null))
      .toList();
    // Setting product entities with null ids
    final List<ProductEntity> products = List.of(
      productsBuilder.get(0).category(categories.get(4)).brand(brands.get(0)).model(models.get(0)).build(),
      productsBuilder.get(1).category(categories.get(4)).brand(brands.get(0)).model(models.get(2)).build(),
      productsBuilder.get(2).category(categories.get(5)).brand(brands.get(2)).model(models.get(4)).build()
    );

    //Persisting entities
    categories.forEach(this.entityManager::persist);
    brands.forEach(this.entityManager::persist);
    models.forEach(this.entityManager::persist);
    products.forEach(this.entityManager::persist);

    final String brandName = "logitech";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByBrandName(brandName, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(2);
    assertThat(productsPage.getTotalElements()).isEqualTo(2);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(2);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> assertThat(product.getBrand().getName()).isEqualTo(brandName));

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(products.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(products.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(products.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(products.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(products.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(products.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(products.get(0).getUpdatedAt());
    assertThat(productsPage.getContent().get(0).getCategory()).usingRecursiveComparison().isEqualTo(products.get(0).getCategory());
    assertThat(productsPage.getContent().get(0).getModel()).usingRecursiveComparison().isEqualTo(products.get(0).getModel());

    assertThat(productsPage.getContent().get(1).getId()).isEqualTo(products.get(1).getId());
    assertThat(productsPage.getContent().get(1).getName()).isEqualTo(products.get(1).getName());
    assertThat(productsPage.getContent().get(1).getDescription()).isEqualTo(products.get(1).getDescription());
    assertThat(productsPage.getContent().get(1).getUnitPrice().toString()).isEqualTo(products.get(1).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(1).getQuantity()).isEqualTo(products.get(1).getQuantity());
    assertThat(productsPage.getContent().get(1).getCreatedAt()).isEqualTo(products.get(1).getCreatedAt());
    assertThat(productsPage.getContent().get(1).getUpdatedAt()).isEqualTo(products.get(1).getUpdatedAt());
    assertThat(productsPage.getContent().get(1).getCategory()).usingRecursiveComparison().isEqualTo(products.get(1).getCategory());
    assertThat(productsPage.getContent().get(1).getModel()).usingRecursiveComparison().isEqualTo(products.get(1).getModel());
  }

  @Test
  @DisplayName("findByModelNameAndBrandNameReturnsProducts - Should successfully find all products of the given model and brand name and return a page of products")
  void findByModelNameAndBrandNameReturnsProducts() {
    final List<CategoryEntity> categories = this.dataMock.getCategoriesEntity()
      .stream()
      .peek(category -> category.setId(null))
      .toList();
    final List<BrandEntity> brands = this.dataMock.getBrandsEntity()
      .stream()
      .map(brand -> BrandEntity.mutate(brand).id(null).build())
      .toList();

    final List<ModelEntity.Builder> modelsBuilder = this.dataMock.getModelsEntity()
      .stream()
      .map(model -> ModelEntity.mutate(model).id(null))
      .toList();
    // Setting model's brand with null ids
    final List<ModelEntity> models = List.of(
      modelsBuilder.get(0).brand(brands.get(0)).build(),
      modelsBuilder.get(1).brand(brands.get(1)).build(),
      modelsBuilder.get(2).brand(brands.get(0)).build(),
      modelsBuilder.get(3).brand(brands.get(0)).build(),
      modelsBuilder.get(4).brand(brands.get(2)).build()
    );

    final List<ProductEntity.Builder> productsBuilder = this.dataMock.getProductsEntity()
      .stream()
      .map(product -> ProductEntity.mutate(product).id(null))
      .toList();
    // Setting product entities with null ids
    final List<ProductEntity> products = List.of(
      productsBuilder.get(0).category(categories.get(4)).brand(brands.get(0)).model(models.get(0)).build(),
      productsBuilder.get(1).category(categories.get(4)).brand(brands.get(0)).model(models.get(2)).build(),
      productsBuilder.get(2).category(categories.get(5)).brand(brands.get(2)).model(models.get(4)).build()
    );

    //Persisting entities
    categories.forEach(this.entityManager::persist);
    brands.forEach(this.entityManager::persist);
    models.forEach(this.entityManager::persist);
    products.forEach(this.entityManager::persist);

    final String modelName = "g pro";
    final String brandName = "logitech";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByModelNameAndBrandName(modelName, brandName, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(1);
    assertThat(productsPage.getTotalElements()).isEqualTo(1);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(1);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> {
        assertThat(product.getModel().getName()).isEqualTo(modelName);
        assertThat(product.getBrand().getName()).isEqualTo(brandName);
      });

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(products.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(products.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(products.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(products.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(products.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(products.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(products.get(0).getUpdatedAt());
    assertThat(productsPage.getContent().get(0).getCategory()).usingRecursiveComparison().isEqualTo(products.get(0).getCategory());
  }
}
