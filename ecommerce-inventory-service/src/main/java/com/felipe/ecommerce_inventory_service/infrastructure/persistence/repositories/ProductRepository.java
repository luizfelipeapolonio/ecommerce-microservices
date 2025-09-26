package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
  Optional<ProductEntity> findByName(String name);

  @Query("SELECT p FROM ProductEntity p JOIN p.category c WHERE c.name = :name")
  Page<ProductEntity> findByCategoryName(@Param("name") String name, Pageable pageable);

  @Query("SELECT p FROM ProductEntity p JOIN p.brand b WHERE b.name = :name")
  Page<ProductEntity> findByBrandName(@Param("name") String name, Pageable pageable);

  @Query("SELECT p FROM ProductEntity p JOIN p.model m JOIN p.brand b WHERE m.name = :modelName AND b.name = :brandName")
  Page<ProductEntity> findByModelNameAndBrandName(@Param("modelName") String modelName, @Param("brandName") String brandName, Pageable pageable);

  @Query(
    "SELECT p FROM ProductEntity p JOIN p.category c JOIN p.brand b JOIN p.model m " +
    "WHERE (:categoryName IS NULL OR c.name = :categoryName) " +
    "AND (:brandName IS NULL OR b.name = :brandName) " +
    "AND (:modelName IS NULL OR m.name = :modelName)"
  )
  Page<ProductEntity> findByOptionalParameters(@Param("categoryName") String categoryName,
                                               @Param("brandName") String brandName,
                                               @Param("modelName") String modelName,
                                               Pageable pageable);

  // PROMOTION RULES FOR FIXED_AMOUNT
  // - Product unit price must be greater than the promotion minimum price
  // - Product unit price minus promotion discount value,
  // must be greater than 60% of product unit price
  @Modifying
  @Query(
    "UPDATE ProductEntity p " +
    "SET p.withDiscount = true, p.discountType = :#{#promotion.discountType}, p.discountValue = :#{#promotion.discountValue} " +
    "WHERE p.unitPrice > :#{#promotion.minimumPrice} AND " +
    "(:discount_limit IS NULL OR :discount_limit IS NOT NULL) AND " +
    "p.category.id = :categoryId"
  )
  int applyPromotionToCategory(@Param("promotion") PromotionDTO promotion,
                               @Param("discount_limit") String discountLimit,
                               @Param("categoryId") String categoryId);

  @Modifying
  @Query(
    "UPDATE ProductEntity p " +
    "SET p.withDiscount = true, p.discountType = :#{#promotion.discountType}, p.discountValue = :#{#promotion.discountValue} " +
    "WHERE p.unitPrice > :#{#promotion.minimumPrice} AND " +
    "(:discount_limit IS NULL OR :discount_limit IS NOT NULL) AND " +
    "p.brand.id = :brandId"
  )
  int applyPromotionToBrand(@Param("promotion") PromotionDTO promotion,
                            @Param("discount_limit") String discountLimit,
                            @Param("brandId") String brandId);


  @Modifying
  @Query(
    "UPDATE ProductEntity p " +
    "SET p.withDiscount = true, p.discountType = :#{#promotion.discountType}, p.discountValue = :#{#promotion.discountValue} " +
    "WHERE p.unitPrice > :#{#promotion.minimumPrice} AND " +
    "(:discount_limit IS NULL OR :discount_limit IS NOT NULL) AND " +
    "p.model.id = :modelId"
  )
  int applyPromotionToModel(@Param("promotion") PromotionDTO promotion,
                            @Param("discount_limit") String discountLimit,
                            @Param("modelId") String modelId);

  @Modifying
  @Query(
    "UPDATE ProductEntity p " +
    "SET p.withDiscount = true, p.discountType = :#{#promotion.discountType}, p.discountValue = :#{#promotion.discountValue} " +
    "WHERE p.unitPrice > :#{#promotion.minimumPrice} AND " +
    "(:discount_limit IS NULL OR :discount_limit IS NOT NULL) AND " +
    "p.id = :productId"
  )
  int applyPromotionToProduct(@Param("promotion") PromotionDTO promotion,
                              @Param("discount_limit") String discountLimit,
                              @Param("productId") UUID productId);


  @Modifying
  @Query(
    "UPDATE ProductEntity p " +
    "SET p.withDiscount = true, p.discountType = :#{#promotion.discountType}, p.discountValue = :#{#promotion.discountValue} " +
    "WHERE p.unitPrice > :#{#promotion.minimumPrice} AND " +
    "(:discount_limit IS NULL OR :discount_limit IS NOT NULL) AND " +
    "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
    "(:brandId IS NULL OR p.brand.id = :brandId) AND " +
    "(:modelId IS NULL OR p.model.id = :modelId)"
  )
  int applyPromotionToSpecific(@Param("promotion") PromotionDTO promotion,
                               @Param("discount_limit") String discountLimit,
                               @Param("categoryId") String categoryId,
                               @Param("brandId") String brandId,
                               @Param("modelId") String modelId);
}
