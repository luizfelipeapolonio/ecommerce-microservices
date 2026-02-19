package com.felipe.ecommerce_order_service.infrastructure.persistence.entities;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "product_name", nullable = false, length = 300)
  private String productName;

  @Column(name = "product_quantity", nullable = false)
  private int productQuantity;

  @Column(name = "final_price", nullable = false)
  private BigDecimal finalPrice;

  @Column(name = "with_coupon", nullable = false)
  private boolean withCoupon = false;

  @Column(name = "coupon_id")
  private UUID couponId;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(nullable = false, length = 20)
  private String status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  public OrderEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OrderEntity id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public OrderEntity productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public String getProductName() {
    return this.productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public OrderEntity productName(String productName) {
    this.productName = productName;
    return this;
  }

  public int getProductQuantity() {
    return this.productQuantity;
  }

  public void setProductQuantity(int productQuantity) {
    this.productQuantity = productQuantity;
  }

  public OrderEntity productQuantity(int productQuantity) {
    this.productQuantity = productQuantity;
    return this;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public OrderEntity finalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public boolean isWithCoupon() {
    return this.withCoupon;
  }

  public void setWithCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
  }

  public OrderEntity withCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
    return this;
  }

  public UUID getCouponId() {
    return this.couponId;
  }

  public void setCouponId(UUID couponId) {
    this.couponId = couponId;
  }

  public OrderEntity couponId(UUID couponId) {
    this.couponId = couponId;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public OrderEntity customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status.getText();
  }

  public OrderEntity status(OrderStatus status) {
    this.status = status.getText();
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OrderEntity createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }
}
