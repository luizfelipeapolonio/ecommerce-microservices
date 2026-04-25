package com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee;

public enum Distance {
  NEAR(1, 1, 300),
  MEDIUM(2, 301, 600),
  FAR(3, 601, 900),
  VERY_FAR(4, 901, Integer.MAX_VALUE);

  private final int id;
  private final int minKm;
  private final int maxKm;

  Distance(int id, int minKm, int maxKm) {
    this.id = id;
    this.minKm = minKm;
    this.maxKm = maxKm;
  }

  public int id() {
    return this.id;
  }

  public int minKm() {
    return this.minKm;
  }

  public int maxKm() {
    return this.maxKm;
  }

  public static Distance of(int id) {
    return switch (id) {
      case 1 -> NEAR;
      case 2 -> MEDIUM;
      case 3 -> FAR;
      case 4 -> VERY_FAR;
      default -> throw new IllegalArgumentException("The value '" + id + "' is not a valid Distance id");
    };
  }
}
