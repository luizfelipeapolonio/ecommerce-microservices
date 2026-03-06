package com.felipe.ecommerce_payment_service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
  private UUID id;
  private UUID orderId;
  private BigDecimal orderAmount;
  private UUID customerId;
  private String checkoutId;
  private PaymentStatus status = PaymentStatus.PENDING;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Payment() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Payment id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public Payment orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public BigDecimal getOrderAmount() {
    return this.orderAmount;
  }

  public void setOrderAmount(BigDecimal orderAmount) {
    this.orderAmount = orderAmount;
  }

  public Payment orderAmount(BigDecimal orderAmount) {
    this.orderAmount = orderAmount;
    return this;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public Payment customerId(UUID customerId) {
    this.customerId = customerId;
    return this;
  }

  public String getCheckoutId() {
    return this.checkoutId;
  }

  public void setCheckoutId(String checkoutId) {
    this.checkoutId = checkoutId;
  }

  public Payment checkoutId(String checkoutId) {
    this.checkoutId = checkoutId;
    return this;
  }

  public PaymentStatus getStatus() {
    return this.status;
  }

  public void setStatus(PaymentStatus status) {
    this.status = status;
  }

  public Payment status(PaymentStatus status) {
    this.status = status;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Payment createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Payment updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
