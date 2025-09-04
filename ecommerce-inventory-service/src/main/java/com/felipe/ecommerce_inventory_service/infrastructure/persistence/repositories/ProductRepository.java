package com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
