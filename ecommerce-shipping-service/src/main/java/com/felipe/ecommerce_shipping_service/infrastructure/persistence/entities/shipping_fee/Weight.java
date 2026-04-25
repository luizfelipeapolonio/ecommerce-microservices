package com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee;

public enum Weight {
  LIGHT(1, 1, 30),
  MODERATE(2, 31, 60),
  HEAVY(3, 61, 100),
  VERY_HEAVY(4, 101, 160);

  private final int id;
  private final int minKg;
  private final int maxKg;

  Weight(int id, int minKg, int maxKg) {
    this.id = id;
    this.minKg = minKg;
    this.maxKg = maxKg;
  }

  public int id() {
    return this.id;
  }

  public int minKg() {
    return this.minKg;
  }

  public int maxKg() {
    return this.maxKg;
  }

  public static Weight of(int id) {
    return switch (id) {
      case 1 -> LIGHT;
      case 2 -> MODERATE;
      case 3 -> HEAVY;
      case 4 -> VERY_HEAVY;
      default -> throw new IllegalArgumentException("The value '" + id + "' is not a valid Weight id");
    };
  }
}
