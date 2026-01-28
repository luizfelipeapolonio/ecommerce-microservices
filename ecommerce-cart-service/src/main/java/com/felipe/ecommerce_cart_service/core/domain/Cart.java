package com.felipe.ecommerce_cart_service.core.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Cart {
  private UUID id;
  private UUID customerId;
  private LocalDateTime createdAt;
  private List<CartItem> items = new ArrayList<>();

  public Cart() {
  }

  public Cart(UUID customerId) {
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

  public List<CartItem> getItems() {
    return this.items;
  }

  public void setItems(List<CartItem> items) {
    this.items = items;
  }

  public void addItem(CartItem item) {
    this.items.add(item);
    item.setCart(this);
  }

  public void removeItem(CartItem item) {
    this.items.remove(item);
    item.setCart(null);
  }
}