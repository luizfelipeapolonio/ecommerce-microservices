package com.felipe.ecommerce_discount_service.core.domain.enums;

import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidDiscountTypeException;

public enum DiscountType {
  PERCENTAGE("percentage"),
  FIXED_AMOUNT("fixed_amount");

  private final String text;

  DiscountType(String text) {
    this.text = text;
  }

  public String getText() {
    return this.text;
  }

  public static DiscountType of(String text) {
    return switch(text) {
      case "percentage" -> PERCENTAGE;
      case "fixed_amount" -> FIXED_AMOUNT;
      default -> throw new InvalidDiscountTypeException(text);
    };
  }
}
