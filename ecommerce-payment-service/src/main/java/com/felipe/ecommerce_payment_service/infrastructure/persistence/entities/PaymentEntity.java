package com.felipe.ecommerce_payment_service.infrastructure.persistence.entities;

import com.felipe.ecommerce_payment_service.core.domain.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
public class PaymentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Column(name = "order_amount", nullable = false)
  private BigDecimal orderAmount;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @Column(name = "checkout_id", nullable = false)
  private String checkoutId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status = PaymentStatus.PENDING;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public PaymentEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public PaymentEntity id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public PaymentEntity orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public BigDecimal getOrderAmount() {
    return this.orderAmount;
  }

  public void setOrderAmount(BigDecimal orderAmount) {
    this.orderAmount = orderAmount;
  }

  public PaymentEntity orderAmount(BigDecimal orderAmount) {
    this.orderAmount = orderAmount;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public PaymentEntity customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getCheckoutId() {
    return this.checkoutId;
  }

  public void setCheckoutId(String checkoutId) {
    this.checkoutId = checkoutId;
  }

  public PaymentEntity checkoutId(String checkoutId) {
    this.checkoutId = checkoutId;
    return this;
  }

  public PaymentStatus getStatus() {
    return this.status;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

  public PaymentEntity status(PaymentStatus status) {
    this.status = status;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public PaymentEntity createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public PaymentEntity updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
