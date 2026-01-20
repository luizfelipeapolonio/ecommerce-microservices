package com.felipe.ecommerce_cart_service.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
public class CartItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "product_name", nullable = false, length = 300)
  private String productName;

  @Column(name = "thumbnail_image", nullable = false, columnDefinition = "TEXT")
  private String thumbnailImage;

  @Column(name = "unit_price", nullable = false)
  private BigDecimal unitPrice;

  @Column(name = "discount_type", length = 12)
  private String discountType;

  @Column(name = "discount_value", length = 50)
  private String discountValue;

  @Column(nullable = false)
  private Integer quantity;

  @CreationTimestamp
  @Column(name = "added_at", nullable = false)
  private LocalDateTime addedAt;

  @ManyToOne
  @JoinColumn(name = "cart_id", nullable = false)
  private CartEntity cart;

  protected CartItemEntity() {
  }

  protected CartItemEntity(Builder builder) {
    this.id = builder.id;
    this.productId = builder.productId;
    this.productName = builder.productName;
    this.thumbnailImage = builder.thumbnailImage;
    this.unitPrice = builder.unitPrice;
    this.discountType = builder.discountType;
    this.discountValue = builder.discountValue;
    this.quantity = builder.quantity;
    this.addedAt = builder.addedAt;
    this.cart = builder.cart;
  }

  public Long getId() {
    return this.id;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public Integer getQuantity() {
    return this.quantity;
  }

  public String getProductName() {
    return this.productName;
  }

  public String getThumbnailImage() {
    return this.thumbnailImage;
  }

  public BigDecimal getUnitPrice() {
    return this.unitPrice;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public String getDiscountValue() {
    return this.discountValue;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public CartEntity getCart() {
    return this.cart;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(CartItemEntity cartItemEntity) {
    return new Builder(cartItemEntity);
  }

  public static class Builder {
    private Long id;
    private UUID productId;
    private String productName;
    private String thumbnailImage;
    private BigDecimal unitPrice;
    private String discountType;
    private String discountValue;
    private Integer quantity;
    private LocalDateTime addedAt;
    private CartEntity cart;

    private Builder() {
    }

    private Builder(CartItemEntity entity) {
      this.id = entity.getId();
      this.productId = entity.getProductId();
      this.productName = entity.getProductName();
      this.thumbnailImage = entity.getThumbnailImage();
      this.unitPrice = entity.getUnitPrice();
      this.discountType = entity.getDiscountType();
      this.discountValue = entity.getDiscountValue();
      this.quantity = entity.getQuantity();
      this.addedAt = entity.getAddedAt();
      this.cart = entity.getCart();
    }

    public Builder id(Long id) {
      this.id = id;
      return this;
    }

    public Builder productId(UUID productId) {
      this.productId = productId;
      return this;
    }

    public Builder productName(String productName) {
      this.productName = productName;
      return this;
    }

    public Builder thumbnailImage(String thumbnailImage) {
      this.thumbnailImage = thumbnailImage;
      return this;
    }

    public Builder unitPrice(BigDecimal unitPrice) {
      this.unitPrice = unitPrice;
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

    public Builder quantity(Integer quantity) {
      this.quantity = quantity;
      return this;
    }

    public Builder addedAt(LocalDateTime addedAt) {
      this.addedAt = addedAt;
      return this;
    }

    public Builder cart(CartEntity cart) {
      this.cart = cart;
      return this;
    }

    public CartItemEntity build() {
      return new CartItemEntity(this);
    }
  }
}
