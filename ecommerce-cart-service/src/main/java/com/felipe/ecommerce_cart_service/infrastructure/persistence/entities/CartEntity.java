package com.felipe.ecommerce_cart_service.infrastructure.persistence.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "carts")
public class CartEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "customer_id", nullable = false)
  private UUID customerId;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<CartItemEntity> items = new ArrayList<>();

  public CartEntity() {
  }

  public CartEntity(UUID customerId) {
    this.customerId = customerId;
  }

  public UUID getId() {
    return this.id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getCustomerId() {
    return this.customerId;
  }

  public void setCustomerId(UUID customerId) {
    this.customerId = customerId;
  }

  public LocalDateTime getCreatedAt() {
    return this.createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public List<CartItemEntity> getItems() {
    return this.items;
  }

  public void setItems(List<CartItemEntity> items) {
    this.items = items;
  }

  public int addItem(CartItemEntity item) {
    this.items.add(item);
    item.setCart(this);
    return this.items.indexOf(item);
  }

  public void removeItem(CartItemEntity item) {
    this.items.remove(item);
    item.setCart(null);
  }
}
