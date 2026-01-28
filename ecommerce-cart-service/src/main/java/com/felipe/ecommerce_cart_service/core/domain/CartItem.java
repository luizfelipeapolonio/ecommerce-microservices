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
  private BigDecimal finalPrice;
  private Integer quantity;
  private LocalDateTime addedAt;
  private Cart cart;
  
  public CartItem() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CartItem id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public CartItem productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public Integer getQuantity() {
    return this.quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public CartItem quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  public String getProductName() {
    return this.productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public CartItem productName(String productName) {
    this.productName = productName;
    return this;
  }

  public String getThumbnailImage() {
    return this.thumbnailImage;
  }

  public void setThumbnailImage(String thumbnailImage) {
    this.thumbnailImage = thumbnailImage;
  }

  public CartItem thumbnailImage(String thumbnailImage) {
    this.thumbnailImage = thumbnailImage;
    return this;
  }

  public BigDecimal getUnitPrice() {
    return this.unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public CartItem unitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public CartItem discountType(String discountType) {
    this.discountType = discountType;
    return this;
  }

  public String getDiscountValue() {
    return this.discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public CartItem discountValue(String discountValue) {
    this.discountValue = discountValue;
    return this;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public CartItem finalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public CartItem addedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
    return this;
  }

  public Cart getCart() {
    return this.cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }

  public CartItem cart(Cart cart) {
    this.cart = cart;
    return this;
  }
}
