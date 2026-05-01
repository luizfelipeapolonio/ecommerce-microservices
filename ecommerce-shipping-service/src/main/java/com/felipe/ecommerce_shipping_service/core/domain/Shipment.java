package com.felipe.ecommerce_shipping_service.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Shipment {
  private UUID id;
  private UUID orderId;
  private ShipmentStatus status = ShipmentStatus.PREPARING;
  private String trackingCode;
  private String totalWeight;
  private LocalDateTime shippedAt;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private LocalDateTime deliveredAt;
  private String shipmentAddress;

  public Shipment() {
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public Shipment id(UUID id) {
    this.id = id;
    return this;
  }

  public UUID getOrderId() {
    return this.orderId;
  }

  public void setOrderId(UUID orderId) {
    this.orderId = orderId;
  }

  public Shipment orderId(UUID orderId) {
    this.orderId = orderId;
    return this;
  }

  public ShipmentStatus getStatus() {
    return this.status;
  }

  public void setStatus(ShipmentStatus status) {
    this.status = status;
  }

  public Shipment status(ShipmentStatus status) {
    this.status = status;
    return this;
  }

  public String getTrackingCode() {
    return this.trackingCode;
  }

  public void setTrackingCode(String trackingCode) {
    this.trackingCode = trackingCode;
  }

  public Shipment trackingCode(String trackingCode) {
    this.trackingCode = trackingCode;
    return this;
  }

  public String getTotalWeight() {
    return this.totalWeight;
  }

  public void setTotalWeight(String totalWeight) {
    this.totalWeight = totalWeight;
  }

  public Shipment totalWeight(String totalWeight) {
    this.totalWeight = totalWeight;
    return this;
  }

  public LocalDateTime getShippedAt() {
    return this.shippedAt;
  }

  public void setShippedAt(LocalDateTime shippedAt) {
    this.shippedAt = shippedAt;
  }

  public Shipment shippedAt(LocalDateTime shippedAt) {
    this.shippedAt = shippedAt;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public Shipment createdAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return this.updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Shipment updatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }

  public LocalDateTime getDeliveredAt() {
    return this.deliveredAt;
  }

  public void setDeliveredAt(LocalDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
  }

  public Shipment deliveredAt(LocalDateTime deliveredAt) {
    this.deliveredAt = deliveredAt;
    return this;
  }

  public String getShipmentAddress() {
    return this.shipmentAddress;
  }

  public void setShipmentAddress(String shipmentAddress) {
    this.shipmentAddress = shipmentAddress;
  }

  public Shipment shipmentAddress(String shipmentAddress) {
    this.shipmentAddress = shipmentAddress;
    return this;
  }
}
