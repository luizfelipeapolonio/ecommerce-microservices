package com.felipe.ecommerce_order_service.infrastructure.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "product_id", nullable = false)
  private UUID productId;

  @Column(name = "product_name", nullable = false, length = 300)
  private String productName;

  private long quantity;

  @Column(name = "final_price", nullable = false)
  private BigDecimal finalPrice;

  @CreationTimestamp
  @Column(name = "added_at", nullable = false)
  private LocalDateTime addedAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  private OrderEntity order;

  public OrderItemEntity() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public OrderItemEntity id(Long id) {
    this.id = id;
    return this;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public OrderItemEntity productId(UUID productId) {
    this.productId = productId;
    return this;
  }

  public String getProductName() {
    return this.productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public OrderItemEntity productName(String productName) {
    this.productName = productName;
    return this;
  }

  public long getQuantity() {
    return this.quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public OrderItemEntity quantity(long quantity) {
    this.quantity = quantity;
    return this;
  }

  public BigDecimal getFinalPrice() {
    return this.finalPrice;
  }

  public void setFinalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
  }

  public OrderItemEntity finalPrice(BigDecimal finalPrice) {
    this.finalPrice = finalPrice;
    return this;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public OrderItemEntity addedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
    return this;
  }

  public OrderEntity getOrder() {
    return this.order;
  }

  public void setOrder(OrderEntity order) {
    this.order = order;
  }

  public OrderItemEntity order(OrderEntity order) {
    this.order = order;
    return this;
  }
}
