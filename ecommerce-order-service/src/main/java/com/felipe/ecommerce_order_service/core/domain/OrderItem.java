package com.felipe.ecommerce_order_service.core.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class OrderItem {
  private long id;
  private UUID productId;
  private String productName;
  private long quantity;
  private BigDecimal finalPrice;
  private LocalDateTime addedAt;
  private Order order;

  public OrderItem() {
  }

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public OrderItem id(long id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public OrderItem productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public OrderItem productName(String productName) {
    this.productName = productName;
    return this;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public OrderItem quantity(long quantity) {
    this.quantity = quantity;
    return this;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public OrderItem finalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public OrderItem addedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
    return this;
  }

  public Order getOrder() {
    return this.order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public OrderItem order(Order order) {
    this.order = order;
    return this;
  }
}
