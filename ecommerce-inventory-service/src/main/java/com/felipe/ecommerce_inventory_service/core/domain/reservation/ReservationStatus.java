package com.felipe.ecommerce_inventory_service.core.domain.reservation;

public enum ReservationStatus {
  CONFIRMED("confirmed"),
  RESERVED("reserved"),
  CANCELLED("cancelled");

  private final String text;

  ReservationStatus(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }

  public static ReservationStatus of(String text) {
    return switch(text) {
      case "confirmed" -> CONFIRMED;
      case "reserved" -> RESERVED;
      case "cancelled" -> CANCELLED;
      default -> throw new IllegalArgumentException("The value '" + text + "' is not a valid ReservationStatus");
    };
  }
}
