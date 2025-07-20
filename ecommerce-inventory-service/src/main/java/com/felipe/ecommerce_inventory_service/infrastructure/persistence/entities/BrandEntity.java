package com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "brands")
public class BrandEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 100)
  private String name;

  private String description;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProductEntity> products;

  @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ModelEntity> models;

  protected BrandEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public List<ProductEntity> getProducts() {
    return this.products;
  }

  public List<ModelEntity> getModels() {
    return this.models;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(BrandEntity brand) {
    return new Builder(brand);
  }

  protected BrandEntity(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.description = builder.description;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.products = builder.products;
    this.models = builder.models;
  }

  public static class Builder {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<ProductEntity> products;
    private List<ModelEntity> models;

    private Builder() {
    }

    private Builder(BrandEntity brand) {
      this.id = brand.getId();
      this.name = brand.getName();
      this.description = brand.getDescription();
      this.createdAt = brand.getCreatedAt();
      this.updatedAt = brand.getUpdatedAt();
      this.products = brand.getProducts();
      this.models = brand.getModels();
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder name(String name) {
      this.name = name;
      return this;
    }

    public Builder description(String description) {
      this.description = description;
      return this;
    }

    public Builder createdAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Builder updatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }

    public Builder products(List<ProductEntity> products) {
      this.products = products;
      return this;
    }

    public Builder models(List<ModelEntity> models) {
      this.models = models;
      return this;
    }

    public BrandEntity build() {
      return new BrandEntity(this);
    }
  }
}
