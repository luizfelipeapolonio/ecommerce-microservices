package com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "parent_category_id")
  private CategoryEntity parentCategory;

  @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CategoryEntity> subCategories;

  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductEntity> products;

  public CategoryEntity() {
  }

  public CategoryEntity(String categoryName) {
    this.name = categoryName;
  }

  public CategoryEntity(String categoryName, CategoryEntity parentCategory) {
    this.name = categoryName;
    this.parentCategory = parentCategory;
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public CategoryEntity getParentCategory() {
    return this.parentCategory;
  }

  public void setParentCategory(CategoryEntity parentCategory) {
    this.parentCategory = parentCategory;
  }

  public List<CategoryEntity> getSubCategories() {
    return this.subCategories;
  }

  public void setSubCategories(List<CategoryEntity> subCategories) {
    this.subCategories = subCategories;
  }

  public List<ProductEntity> getProducts() {
    return this.products;
  }

  public void setProducts(List<ProductEntity> products) {
    this.products = products;
  }
}