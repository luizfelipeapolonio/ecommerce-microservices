package com.felipe.ecommerce_cart_service.core.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class CartItem {
  private Long id;
  private UUID productId;
  private int quantity;
  private LocalDateTime addedAt;
  private Cart cart;

  public CartItem() {
  }

  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getProductId() {
    return this.productId;
  }

  public void setProductId(UUID productId) {
    this.productId = productId;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public  void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public LocalDateTime getAddedAt() {
    return this.addedAt;
  }

  public void setAddedAt(LocalDateTime addedAt) {
    this.addedAt = addedAt;
  }

  public Cart getCart() {
    return this.cart;
  }

  public void setCart(Cart cart) {
    this.cart = cart;
  }
}
