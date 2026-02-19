package com.felipe.ecommerce_order_service.core.domain;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Order {
  private UUID id;
  private UUID productId;
  private String productName;
  private int productQuantity;
  private BigDecimal finalPrice;
  private boolean withCoupon;
  private UUID couponId;
  private UUID customerId;
  private String status;
  private LocalDateTime createdAt;

  public Order() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Order id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public Order  productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public String getProductName() {
    return this.productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public Order productName(String productName) {
    this.productName = productName;
    return this;
  }

  public int getProductQuantity() {
    return this.productQuantity;
  }

  public void setProductQuantity(int productQuantity) {
    this.productQuantity = productQuantity;
  }

  public Order productQuantity(int productQuantity) {
    this.productQuantity = productQuantity;
    return this;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public Order finalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public boolean isWithCoupon() {
    return this.withCoupon;
  }

  public void setWithCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
  }

  public Order withCoupon(boolean withCoupon) {
    this.withCoupon = withCoupon;
    return this;
  }

  public UUID getCouponId() {
    return this.couponId;
  }

  public void setCouponId(UUID couponId) {
    this.couponId = couponId;
  }

  public Order couponId(UUID couponId) {
    this.couponId = couponId;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public Order customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(OrderStatus status) {
    this.status = status.getText();
  }

  public Order status(OrderStatus status) {
    this.status = status.getText();
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Order createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }
}
