package com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipment;

import com.felipe.ecommerce_shipping_service.core.domain.ShipmentStatus;
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

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "shipments")
public class ShipmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "order_id", nullable = false)
  private UUID orderId;

  @Enumerated(EnumType.STRING)
  @Column(length = 30, nullable = false)
  private ShipmentStatus status = ShipmentStatus.PREPARING;

  @Column(name = "tracking_code", nullable = false, unique = true)
  private String trackingCode;

  @Column(name = "total_weight", length = 100, nullable = false)
  private String totalWeight;

  @Column(name = "shipped_at")
  private LocalDateTime shippedAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "delivered_at")
  private LocalDateTime deliveredAt;

  @Column(nullable = false)
  private String shipmentAddress;

  public ShipmentEntity() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public ShipmentEntity id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public ShipmentEntity orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public ShipmentStatus getStatus() {
    return this.status;
  }

  public void setStatus(ShipmentStatus status) {
    this.status = status;
  }

  public ShipmentEntity status(ShipmentStatus status) {
    this.status = status;
    return this;
  }

  public String getTrackingCode() {
    return this.trackingCode;
  }

  public void setTrackingCode(String trackingCode) {
    this.trackingCode = trackingCode;
  }

  public ShipmentEntity trackingCode(String trackingCode) {
    this.trackingCode = trackingCode;
    return this;
  }

  public String getTotalWeight() {
    return this.totalWeight;
  }

  public void setTotalWeight(String totalWeight) {
    this.totalWeight = totalWeight;
  }

  public ShipmentEntity totalWeight(String totalWeight) {
    this.totalWeight = totalWeight;
    return this;
  }

  public LocalDateTime getShippedAt() {
    return this.shippedAt;
  }

  public void setShippedAt(LocalDateTime shippedAt) {
    this.shippedAt = shippedAt;
  }

  public ShipmentEntity shippedAt(LocalDateTime shippedAt) {
    this.shippedAt = shippedAt;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public ShipmentEntity createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public ShipmentEntity updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public LocalDateTime getDeliveredAt() {
    return this.deliveredAt;
  }

  public void setDeliveredAt(LocalDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
  }

  public ShipmentEntity deliveredAt(LocalDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
    return this;
  }

  public String getShipmentAddress() {
    return this.shipmentAddress;
  }

  public void setShipmentAddress(String shipmentAddress) {
    this.shipmentAddress = shipmentAddress;
  }

  public ShipmentEntity shipmentAddress(String shipmentAddress) {
    this.shipmentAddress = shipmentAddress;
    return this;
  }
}
