package com.felipe.ecommerce_cart_service.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

  @Column(name = "final_price", nullable = false)
  private BigDecimal finalPrice;

  @Column(nullable = false)
  private Integer quantity;

  @CreationTimestamp
  @Column(name = "added_at", nullable = false, updatable = false)
  private LocalDateTime addedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", nullable = false)
  private CartEntity cart;

  public CartItemEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CartItemEntity id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public CartItemEntity productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public Integer getQuantity() {
    return this.quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public CartItemEntity quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

  public String getProductName() {
    return this.productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public CartItemEntity productName(String productName) {
    this.productName = productName;
    return this;
  }

  public String getThumbnailImage() {
    return this.thumbnailImage;
  }

  public void setThumbnailImage(String thumbnailImage) {
    this.thumbnailImage = thumbnailImage;
  }

  public CartItemEntity thumbnailImage(String thumbnailImage) {
    this.thumbnailImage = thumbnailImage;
    return this;
  }

  public BigDecimal getUnitPrice() {
    return this.unitPrice;
  }

  public void setUnitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
  }

  public CartItemEntity unitPrice(BigDecimal unitPrice) {
    this.unitPrice = unitPrice;
    return this;
  }

  public String getDiscountType() {
    return this.discountType;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public CartItemEntity discountType(String discountType) {
    this.discountType = discountType;
    return this;
  }

  public String getDiscountValue() {
    return this.discountValue;
  }

  public void setDiscountValue(String discountValue) {
    this.discountValue = discountValue;
  }

  public CartItemEntity discountValue(String discountValue) {
    this.discountValue = discountValue;
    return this;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public CartItemEntity finalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public CartItemEntity addedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
    return this;
  }

  public CartEntity getCart() {
    return this.cart;
  }

  public void setCart(CartEntity cart) {
    this.cart = cart;
  }

  public CartItemEntity cart(CartEntity cart) {
    this.cart = cart;
    return this;
  }
}
