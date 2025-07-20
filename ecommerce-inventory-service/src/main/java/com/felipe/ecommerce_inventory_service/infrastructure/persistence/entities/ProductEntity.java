package com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
public class ProductEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, length = 300)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @Column(name = "price_unity", nullable = false)
  private BigDecimal priceUnity;

  @Column(nullable = false)
  private long quantity;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private CategoryEntity category;

  @ManyToOne
  @JoinColumn(name = "brand_id", nullable = false)
  private BrandEntity brand;

  @ManyToOne
  @JoinColumn(name = "model_id", nullable = false)
  private ModelEntity model;

  protected ProductEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public String getImageUrl() {
    return this.imageUrl;
  }

  public BigDecimal getPriceUnity() {
    return this.priceUnity;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public CategoryEntity getCategory() {
    return this.category;
  }

  public BrandEntity getBrand() {
    return this.brand;
  }

  public ModelEntity getModel() {
    return this.model;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(ProductEntity product) {
    return new Builder(product);
  }

  protected ProductEntity(Builder builder) {
    this.id = builder.id;
    this.name = builder.name;
    this.description = builder.description;
    this.imageUrl = builder.imageUrl;
    this.priceUnity = builder.priceUnity;
    this.quantity = builder.quantity;
    this.createdAt = builder.createdAt;
    this.updatedAt = builder.updatedAt;
    this.category = builder.category;
    this.brand = builder.brand;
    this.model = builder.model;
  }

  public static class Builder {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal priceUnity;
    private long quantity;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private CategoryEntity category;
    private BrandEntity brand;
    private ModelEntity model;

    private Builder() {
    }

    private Builder(ProductEntity product) {
      this.id = product.getId();
      this.name = product.getName();
      this.description = product.getDescription();
      this.imageUrl = product.getImageUrl();
      this.priceUnity = product.getPriceUnity();
      this.quantity = product.getQuantity();
      this.createdAt = product.getCreatedAt();
      this.updatedAt = product.getUpdatedAt();
      this.category = product.getCategory();
      this.brand = product.getBrand();
      this.model = product.getModel();
    }

    public Builder id(UUID id) {
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

    public Builder imageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public Builder priceUnity(BigDecimal priceUnity) {
      this.priceUnity = priceUnity;
      return this;
    }

    public Builder quantity(long quantity) {
      this.quantity = quantity;
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

    public Builder category(CategoryEntity category) {
      this.category = category;
      return this;
    }

    public Builder brand(BrandEntity brand) {
      this.brand = brand;
      return this;
    }

    public Builder model(ModelEntity model) {
      this.model = model;
      return this;
    }

    public ProductEntity build() {
      return new ProductEntity(this);
    }
  }
}
