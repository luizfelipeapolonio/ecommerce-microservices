package com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupon_applied_by")
public class CouponAppliedByEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "order_id", nullable = false, unique = true)
  private UUID orderId;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(name = "applied_at", nullable = false)
  @CreationTimestamp
  private LocalDateTime appliedAt;

  @ManyToOne
  @JoinColumn(name = "coupon_id", nullable = false)
  private CouponEntity coupon;

  public CouponAppliedByEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public CouponAppliedByEntity id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public CouponAppliedByEntity orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public CouponAppliedByEntity customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public LocalDateTime getAppliedAt() {
    return this.appliedAt;
  }

  public void setAppliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
  }

  public CouponAppliedByEntity appliedAt(LocalDateTime appliedAt) {
    this.appliedAt = appliedAt;
    return this;
  }

  public CouponEntity getCoupon() {
    return this.coupon;
  }

  public void setCoupon(CouponEntity coupon) {
    this.coupon = coupon;
  }

  public CouponAppliedByEntity coupon(CouponEntity coupon) {
    this.coupon = coupon;
    return this;
  }
}
