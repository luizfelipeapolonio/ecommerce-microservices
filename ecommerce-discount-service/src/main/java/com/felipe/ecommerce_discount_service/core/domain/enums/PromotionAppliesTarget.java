package com.felipe.ecommerce_discount_service.core.domain.enums;

import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidPromotionAppliesTargetException;

public enum PromotionAppliesTarget {
  PRODUCT("product"),
  CATEGORY("category"),
  BRAND("brand"),
  MODEL("model");

  private final String text;

  PromotionAppliesTarget(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }

  public static PromotionAppliesTarget of(String text) {
    return switch(text) {
      case "product" -> PRODUCT;
      case "category" -> CATEGORY;
      case "brand" -> BRAND;
      case "model" -> MODEL;
      default -> throw new InvalidPromotionAppliesTargetException(text);
    };
  }
}
