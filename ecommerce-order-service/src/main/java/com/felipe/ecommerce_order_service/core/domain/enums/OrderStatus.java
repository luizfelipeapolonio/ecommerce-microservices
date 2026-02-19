package com.felipe.ecommerce_order_service.core.domain.enums;

public enum OrderStatus {
  CREATED("created"),
  PENDING("pending"),
  CANCELLED("cancelled");

  private final String text;

  OrderStatus(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }

  public static OrderStatus of(String orderStatus) {
    return switch(orderStatus) {
      case "created" -> CREATED;
      case "pending" -> PENDING;
      case "cancelled" -> CANCELLED;
      default -> throw new IllegalArgumentException("");
    };
  }
}
