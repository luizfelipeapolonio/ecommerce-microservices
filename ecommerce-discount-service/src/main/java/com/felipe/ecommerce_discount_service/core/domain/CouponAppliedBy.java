package com.felipe.ecommerce_discount_service.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class CouponAppliedBy {
  private Long id;
  private UUID orderId;
  private UUID customerId;
  private LocalDateTime appliedAt;
  private Coupon coupon;

  public CouponAppliedBy() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CouponAppliedBy id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public CouponAppliedBy orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public CouponAppliedBy customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public LocalDateTime getAppliedAt() {
    return this.appliedAt;
  }

  public void setAppliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public CouponAppliedBy appliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
    return this;
  }

  public Coupon getCoupon() {
    return this.coupon;
  }

  public void setCoupon(Coupon coupon) {
    this.coupon = coupon;
  }

  public CouponAppliedBy coupon(Coupon coupon) {
    this.coupon = coupon;
    return this;
  }
}
