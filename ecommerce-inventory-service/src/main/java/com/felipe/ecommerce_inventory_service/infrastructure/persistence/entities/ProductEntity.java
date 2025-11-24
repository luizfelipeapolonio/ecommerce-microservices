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

  @Column(name = "unit_price", nullable = false)
  private BigDecimal unitPrice;

  @Column(nullable = false)
  private long quantity;

  @Column(name = "with_discount", nullable = false)
  private boolean withDiscount = false;

  @Column(name = "promotion_id")
  private String promotionId;

  @Column(name = "discount_type", length = 12)
  private String discountType;

  @Column(name = "discount_value", length = 50)
  private String discountValue;

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

  public BigDecimal getUnitPrice() {
    return this.unitPrice;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public boolean isItWithDiscount() {
    return this.withDiscount;
  }

  public String getPromotionId() {
    return this.promotionId;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public String getDiscountValue() {
    return this.discountValue;
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
    this.unitPrice = builder.unitPrice;
    this.quantity = builder.quantity;
    this.withDiscount = builder.withDiscount;
    this.promotionId = builder.promotionId;
    this.discountType = builder.discountType;
    this.discountValue = builder.discountValue;
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
    private BigDecimal unitPrice;
    private long quantity;
    private boolean withDiscount = false;
    private String promotionId;
    private String discountType;
    private String discountValue;
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
      this.unitPrice = product.getUnitPrice();
      this.quantity = product.getQuantity();
      this.withDiscount = product.isItWithDiscount();
      this.promotionId = product.getPromotionId();
      this.discountType = product.getDiscountType();
      this.discountValue = product.getDiscountValue();
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

    public Builder unitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
      return this;
    }

    public Builder quantity(long quantity) {
      this.quantity = quantity;
      return this;
    }

    public Builder withDiscount(boolean withDiscount) {
      this.withDiscount = withDiscount;
      return this;
    }

    public Builder promotionId(String promotionId) {
      this.promotionId = promotionId;
      return this;
    }

    public Builder discountType(String discountType) {
      this.discountType = discountType;
      return this;
    }

    public Builder discountValue(String discountValue) {
      this.discountValue = discountValue;
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
