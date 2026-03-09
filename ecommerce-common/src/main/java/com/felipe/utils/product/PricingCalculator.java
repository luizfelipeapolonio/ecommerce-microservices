package com.felipe.utils.product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PricingCalculator {
  public static BigDecimal calculateFinalPrice(
    String discountType,
    String unitPrice,
    String discountValue,
    long productQuantity
  ) throws PriceCalculationException {
    final BigDecimal price = new BigDecimal(unitPrice);
    final BigDecimal quantity = new BigDecimal(productQuantity);
    BigDecimal finalPrice;
    if (discountType != null) {
      final BigDecimal discount = calculateDiscount(unitPrice, discountType, discountValue);
      finalPrice = price.multiply(quantity).subtract(discount);
    } else {
      finalPrice = price.multiply(quantity);
    }
    return finalPrice;
  }

  public static BigDecimal calculateDiscount(String unitPrice, String discountType, String discountValue) {
    final BigDecimal price = new BigDecimal(unitPrice);
    final BigDecimal discount = new BigDecimal(discountValue);
    return switch (discountType) {
      case "fixed_amount" -> discount;
      case "percentage" -> discount.divide(new BigDecimal(100))
        .multiply(price)
        .setScale(2, RoundingMode.HALF_DOWN);
      default -> throw new PriceCalculationException("The discount type '" + discountType + "' is not supported");
    };
  }

  public static String formattedDiscountPercentageString(String discountValue) {
    return new BigDecimal(discountValue)
      .setScale(1, RoundingMode.HALF_DOWN)
      .stripTrailingZeros()
      .toPlainString();
  }
}
