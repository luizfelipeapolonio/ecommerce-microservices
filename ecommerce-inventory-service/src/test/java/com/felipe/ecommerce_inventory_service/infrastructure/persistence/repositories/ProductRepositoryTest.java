package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles(value = "test")
public class ProductRepositoryTest {

  @Autowired
  EntityManager entityManager;

  @Autowired
  ProductRepository productRepository;
  private List<ProductEntity> productsMock;

  @BeforeEach
  void setUp() {
    this.productsMock = setUpDatabaseData();
  }

  @Test
  @DisplayName("findByCategoryNameReturnsProducts - Should successfully find all products of the given category name and return a page of products")
  void findByCategoryNameReturnsProducts() {
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

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());
    assertThat(productsPage.getContent().get(0).getBrand()).usingRecursiveComparison().isEqualTo(this.productsMock.get(0).getBrand());
    assertThat(productsPage.getContent().get(0).getModel()).usingRecursiveComparison().isEqualTo(this.productsMock.get(0).getModel());

    assertThat(productsPage.getContent().get(1).getId()).isEqualTo(this.productsMock.get(1).getId());
    assertThat(productsPage.getContent().get(1).getName()).isEqualTo(this.productsMock.get(1).getName());
    assertThat(productsPage.getContent().get(1).getDescription()).isEqualTo(this.productsMock.get(1).getDescription());
    assertThat(productsPage.getContent().get(1).getUnitPrice().toString()).isEqualTo(this.productsMock.get(1).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(1).getQuantity()).isEqualTo(this.productsMock.get(1).getQuantity());
    assertThat(productsPage.getContent().get(1).getCreatedAt()).isEqualTo(this.productsMock.get(1).getCreatedAt());
    assertThat(productsPage.getContent().get(1).getUpdatedAt()).isEqualTo(this.productsMock.get(1).getUpdatedAt());
    assertThat(productsPage.getContent().get(1).getBrand()).usingRecursiveComparison().isEqualTo(this.productsMock.get(1).getBrand());
    assertThat(productsPage.getContent().get(1).getModel()).usingRecursiveComparison().isEqualTo(this.productsMock.get(1).getModel());
  }

  @Test
  @DisplayName("findByBrandNameReturnsProducts - Should successfully find all products of the given brand name and return a page of products")
  void findByBrandNameReturnsProducts() {
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

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());
    assertThat(productsPage.getContent().get(0).getCategory()).usingRecursiveComparison().isEqualTo(this.productsMock.get(0).getCategory());
    assertThat(productsPage.getContent().get(0).getModel()).usingRecursiveComparison().isEqualTo(this.productsMock.get(0).getModel());

    assertThat(productsPage.getContent().get(1).getId()).isEqualTo(this.productsMock.get(1).getId());
    assertThat(productsPage.getContent().get(1).getName()).isEqualTo(this.productsMock.get(1).getName());
    assertThat(productsPage.getContent().get(1).getDescription()).isEqualTo(this.productsMock.get(1).getDescription());
    assertThat(productsPage.getContent().get(1).getUnitPrice().toString()).isEqualTo(this.productsMock.get(1).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(1).getQuantity()).isEqualTo(this.productsMock.get(1).getQuantity());
    assertThat(productsPage.getContent().get(1).getCreatedAt()).isEqualTo(this.productsMock.get(1).getCreatedAt());
    assertThat(productsPage.getContent().get(1).getUpdatedAt()).isEqualTo(this.productsMock.get(1).getUpdatedAt());
    assertThat(productsPage.getContent().get(1).getCategory()).usingRecursiveComparison().isEqualTo(this.productsMock.get(1).getCategory());
    assertThat(productsPage.getContent().get(1).getModel()).usingRecursiveComparison().isEqualTo(this.productsMock.get(1).getModel());
  }

  @Test
  @DisplayName("findByModelNameAndBrandNameReturnsProducts - Should successfully find all products of the given model and brand name and return a page of products")
  void findByModelNameAndBrandNameReturnsProducts() {
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

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());
    assertThat(productsPage.getContent().get(0).getCategory()).usingRecursiveComparison().isEqualTo(this.productsMock.get(0).getCategory());
  }

  @Test
  @DisplayName("findByOptionalParametersWithAllParameters - Should successfully find and return the products with all the given parameters")
  void findByOptionalParametersWithAllParameters() {
    final String categoryName = "mouse";
    final String brandName = "logitech";
    final String modelName = "g pro";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByOptionalParameters(categoryName, brandName, modelName, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(1);
    assertThat(productsPage.getTotalElements()).isEqualTo(1);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(1);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> {
        assertThat(product.getCategory().getName()).isEqualTo(categoryName);
        assertThat(product.getBrand().getName()).isEqualTo(brandName);
        assertThat(product.getModel().getName()).isEqualTo(modelName);
      });

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());
  }

  @Test
  @DisplayName("findByOptionalParametersWithCategoryNull - Should successfully find and return the products with the given brand and model only")
  void findByOptionalParametersWithCategoryNull() {
    final String brandName = "logitech";
    final String modelName = "g pro";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByOptionalParameters(null,brandName, modelName, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(1);
    assertThat(productsPage.getTotalElements()).isEqualTo(1);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(1);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> {
        assertThat(product.getBrand().getName()).isEqualTo(brandName);
        assertThat(product.getModel().getName()).isEqualTo(modelName);
      });

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());
  }

  @Test
  @DisplayName("findByOptionalParametersWithModelNull - Should successfully find and return the products with the given category and brand only")
  void findByOptionalParametersWithModelNull() {
    final String categoryName = "mouse";
    final String brandName = "logitech";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByOptionalParameters(categoryName, brandName, null, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(2);
    assertThat(productsPage.getTotalElements()).isEqualTo(2);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(2);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> {
        assertThat(product.getCategory().getName()).isEqualTo(categoryName);
        assertThat(product.getBrand().getName()).isEqualTo(brandName);
      });

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());

    assertThat(productsPage.getContent().get(1).getId()).isEqualTo(this.productsMock.get(1).getId());
    assertThat(productsPage.getContent().get(1).getName()).isEqualTo(this.productsMock.get(1).getName());
    assertThat(productsPage.getContent().get(1).getDescription()).isEqualTo(this.productsMock.get(1).getDescription());
    assertThat(productsPage.getContent().get(1).getUnitPrice().toString()).isEqualTo(this.productsMock.get(1).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(1).getQuantity()).isEqualTo(this.productsMock.get(1).getQuantity());
    assertThat(productsPage.getContent().get(1).getCreatedAt()).isEqualTo(this.productsMock.get(1).getCreatedAt());
    assertThat(productsPage.getContent().get(1).getUpdatedAt()).isEqualTo(this.productsMock.get(1).getUpdatedAt());
  }

  @Test
  @DisplayName("findByOptionalParametersWithBrandNull - Should successfully find and return the products with the given category and model only")
  void findByOptionalParametersWithBrandNull() {
    final String categoryName = "mouse";
    final String modelName = "g pro";
    Pageable pagination = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductEntity> productsPage = this.productRepository.findByOptionalParameters(categoryName, null, modelName, pagination);

    // Pagination assertion
    assertThat(productsPage.getNumber()).isEqualTo(pagination.getPageNumber());
    assertThat(productsPage.getNumberOfElements()).isEqualTo(1);
    assertThat(productsPage.getTotalElements()).isEqualTo(1);
    assertThat(productsPage.getTotalPages()).isEqualTo(1);
    // Content assertion
    assertThat(productsPage.getContent().size()).isEqualTo(1);
    assertThat(productsPage.getContent())
      .allSatisfy(product -> {
        assertThat(product.getCategory().getName()).isEqualTo(categoryName);
        assertThat(product.getModel().getName()).isEqualTo(modelName);
      });

    assertThat(productsPage.getContent().get(0).getId()).isEqualTo(this.productsMock.get(0).getId());
    assertThat(productsPage.getContent().get(0).getName()).isEqualTo(this.productsMock.get(0).getName());
    assertThat(productsPage.getContent().get(0).getDescription()).isEqualTo(this.productsMock.get(0).getDescription());
    assertThat(productsPage.getContent().get(0).getUnitPrice().toString()).isEqualTo(this.productsMock.get(0).getUnitPrice().toString());
    assertThat(productsPage.getContent().get(0).getQuantity()).isEqualTo(this.productsMock.get(0).getQuantity());
    assertThat(productsPage.getContent().get(0).getCreatedAt()).isEqualTo(this.productsMock.get(0).getCreatedAt());
    assertThat(productsPage.getContent().get(0).getUpdatedAt()).isEqualTo(this.productsMock.get(0).getUpdatedAt());
  }

  @Test
  @DisplayName("applyPromotionToCategorySuccessWithDiscountLimitNotNull - Should successfully apply a promotion to all products of a category and return the quantity of applied promotion")
  void applyPromotionToCategorySuccessWithDiscountLimitNotNull() {
    final String categoryId = this.productsMock.get(0).getCategory().getId().toString();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "fixed_amount",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToCategory(promotionDTO, discountLimit, categoryId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(2);
  }

  @Test
  @DisplayName("applyPromotionToCategorySuccessWithDiscountLimitNull - Should successfully apply a promotion to all products of a category and return the quantity of applied promotion")
  void applyPromotionToCategorySuccessWithDiscountLimitNull() {
    final String categoryId = this.productsMock.get(0).getCategory().getId().toString();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToCategory(promotionDTO, null, categoryId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(2);
  }

  @Test
  @DisplayName("applyPromotionToBrandSuccessWithDiscountLimitNotNull - Should successfully apply a promotion to all products of a brand and return the quantity of applied promotion")
  void applyPromotionToBrandSuccessWithDiscountLimitNotNull() {
    final String brandId = this.productsMock.get(0).getBrand().getId().toString();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToBrand(promotionDTO, discountLimit, brandId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(2);
  }

  @Test
  @DisplayName("applyPromotionToBrandSuccessWithDiscountLimitNull - Should successfully apply a promotion to all products of a brand and return the quantity of applied promotion")
  void applyPromotionToBrandSuccessWithDiscountLimitNull() {
    final String brandId = this.productsMock.get(0).getBrand().getId().toString();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToBrand(promotionDTO, null, brandId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(2);
  }

  @Test
  @DisplayName("applyPromotionToModelSuccessWithDiscountLimitNotNull - Should successfully apply a promotion to all products of a model and return the quantity of applied promotion")
  void applyPromotionToModelSuccessWithDiscountLimitNotNull() {
    final String modelId = this.productsMock.get(0).getModel().getId().toString();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToModel(promotionDTO, discountLimit, modelId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToModelSuccessWithDiscountLimitNull - Should successfully apply a promotion to all products of a model and return the quantity of applied promotion")
  void applyPromotionToModelSuccessWithDiscountLimitNull() {
    final String modelId = this.productsMock.get(0).getModel().getId().toString();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToModel(promotionDTO, null, modelId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToProductSuccessWithDiscountLimitNotNull - Should successfully apply promotion to product and return the quantity of applied promotion")
  void applyPromotionToProductSuccessWithDiscountLimitNotNull() {
    final UUID productId = this.productsMock.get(0).getId();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToProduct(promotionDTO, discountLimit, productId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToProductSuccessWithDiscountLimitNull - Should successfully apply promotion to product and return the quantity of applied promotion")
  void applyPromotionToProductSuccessWithDiscountLimitNull() {
    final UUID productId = this.productsMock.get(0).getId();
    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToProduct(promotionDTO, null, productId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToSpecificWithAllParameters - Should successfully apply promotion to product with the given parameters")
  void applyPromotionToSpecificWithAllParameters() {
    final String categoryId = this.productsMock.get(0).getCategory().getId().toString();
    final String brandId = this.productsMock.get(0).getBrand().getId().toString();
    final String modelId = this.productsMock.get(0).getModel().getId().toString();

    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToSpecific(promotionDTO, discountLimit, categoryId, brandId, modelId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToSpecificWithModelIdNull - Should successfully apply promotion to product with the given parameters")
  void applyPromotionToSpecificWithModelIdNull() {
    final String categoryId = this.productsMock.get(0).getCategory().getId().toString();
    final String brandId = this.productsMock.get(0).getBrand().getId().toString();

    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToSpecific(promotionDTO, discountLimit, categoryId, brandId, null);

    assertThat(quantityOfAppliedPromotion).isEqualTo(2);
  }

  @Test
  @DisplayName("applyPromotionToSpecificWithBrandIdNull - Should successfully apply promotion to product with the given parameters")
  void applyPromotionToSpecificWithBrandIdNull() {
    final String categoryId = this.productsMock.get(0).getCategory().getId().toString();
    final String modelId = this.productsMock.get(0).getModel().getId().toString();

    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToSpecific(promotionDTO, discountLimit, categoryId, null, modelId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToSpecificWithCategoryIdNull - Should successfully apply promotion to product with the given parameters")
  void applyPromotionToSpecificWithCategoryIdNull() {
    final String brandId = this.productsMock.get(0).getBrand().getId().toString();
    final String modelId = this.productsMock.get(0).getModel().getId().toString();

    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String discountLimit = "(p.unitPrice - :#{#promotion.discountValue}) > (p.unitPrice * 0.6)";

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToSpecific(promotionDTO, discountLimit, null, brandId, modelId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  @Test
  @DisplayName("applyPromotionToSpecificWithDiscountLimitNull - Should successfully apply promotion to product with the given parameters")
  void applyPromotionToSpecificWithDiscountLimitNull() {
    final String categoryId = this.productsMock.get(0).getCategory().getId().toString();
    final String brandId = this.productsMock.get(0).getBrand().getId().toString();
    final String modelId = this.productsMock.get(0).getModel().getId().toString();

    final PromotionDTO promotionDTO = new PromotionDTO(
      "84eb4e8a-84cc-408b-8980-3e8c269138ba",
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );

    int quantityOfAppliedPromotion = this.productRepository.applyPromotionToSpecific(promotionDTO, null, categoryId, brandId, modelId);

    assertThat(quantityOfAppliedPromotion).isEqualTo(1);
  }

  private List<ProductEntity> setUpDatabaseData() {
    final DataMock mock = new DataMock();
    final List<CategoryEntity> categories = mock.getCategoriesEntity()
      .stream()
      .peek(category -> category.setId(null))
      .toList();
    final List<BrandEntity> brands = mock.getBrandsEntity()
      .stream()
      .map(brand -> BrandEntity.mutate(brand).id(null).build())
      .toList();

    final List<ModelEntity.Builder> modelsBuilder = mock.getModelsEntity()
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

    final List<ProductEntity.Builder> productsBuilder = mock.getProductsEntity()
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

    return products;
  }
}
