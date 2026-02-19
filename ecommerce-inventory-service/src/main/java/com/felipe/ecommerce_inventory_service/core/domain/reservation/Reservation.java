package com.felipe.ecommerce_inventory_service.core.domain.reservation;

import java.time.LocalDateTime;
import java.util.UUID;

public class Reservation {
  private Long id;
  private UUID productId;
  private UUID orderId;
  private int quantity;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public Reservation() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Reservation id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public Reservation productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public Reservation orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public Reservation quantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status.getText();
  }

  public Reservation status(ReservationStatus status) {
    this.status = status.getText();
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Reservation createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Reservation updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  @Override
  public String toString() {
    return "Reservation{" +
      "id=" + id +
      ", productId=" + productId +
      ", orderId=" + orderId +
      ", quantity=" + quantity +
      ", status='" + status + '\'' +
      ", createdAt=" + createdAt +
      ", updatedAt=" + updatedAt +
      '}';
  }
}
