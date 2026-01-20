package com.felipe.ecommerce_cart_service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class CartItem {
  private Long id;
  private UUID productId;
  private String productName;
  private String thumbnailImage;
  private BigDecimal unitPrice;
  private String discountType;
  private String discountValue;
  private Integer quantity;
  private LocalDateTime addedAt;
  private Cart cart;

  private CartItem() {
  }

  private CartItem(Builder builder) {
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

  public int getQuantity() {
    return this.quantity;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public Cart getCart() {
    return this.cart;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder mutate(CartItem cartItem) {
    return new Builder(cartItem);
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
    private Cart cart;

    private Builder() {
    }

    private Builder(CartItem item) {
      this.id = item.getId();
      this.productId = item.getProductId();
      this.productName = item.getProductName();
      this.thumbnailImage = item.getThumbnailImage();
      this.unitPrice = item.getUnitPrice();
      this.discountType = item.getDiscountType();
      this.discountValue = item.getDiscountValue();
      this.quantity = item.getQuantity();
      this.addedAt = item.getAddedAt();
      this.cart = item.getCart();
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

    public Builder cart(Cart cart) {
      this.cart = cart;
      return this;
    }

    public CartItem build() {
      return new CartItem(this);
    }
  }
}
