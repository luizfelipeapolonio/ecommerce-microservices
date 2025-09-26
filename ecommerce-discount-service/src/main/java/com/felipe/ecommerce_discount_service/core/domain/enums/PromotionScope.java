package com.felipe.ecommerce_discount_service.core.domain.enums;

import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidPromotionScopeException;

public enum PromotionScope {
  ALL("all"),
  SPECIFIC("specific");

  private final String text;

  PromotionScope(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }

  public static PromotionScope of(String text) {
    return switch(text) {
      case "all" -> ALL;
      case "specific" -> SPECIFIC;
      default -> throw new InvalidPromotionScopeException(text);
    };
  }
}
