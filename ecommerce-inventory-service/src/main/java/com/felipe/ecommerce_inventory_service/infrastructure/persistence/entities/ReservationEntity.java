package com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities;

import com.felipe.ecommerce_inventory_service.core.domain.reservation.ReservationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class ReservationEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "order_id", nullable = false, unique = true)
  private UUID orderId;

  private int quantity;

  @Column(nullable = false)
  private String status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  public ReservationEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public ReservationEntity id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public ReservationEntity productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public ReservationEntity orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public ReservationEntity quantity(int quantity) {
    this.quantity = quantity;
    return this;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(ReservationStatus status) {
    this.status = status.getText();
  }

  public ReservationEntity status(ReservationStatus status) {
    this.status = status.getText();
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ReservationEntity createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public ReservationEntity updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
