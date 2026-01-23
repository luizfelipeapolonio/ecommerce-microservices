package com.felipe.utils.product;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ProductUtils {
  public static BigDecimal calculateFinalPrice(
    String discountType,
    String unitPrice,
    String discountValue,
    int productQuantity
  ) throws PriceCalculationException {
    final BigDecimal price = new BigDecimal(unitPrice);
    final BigDecimal discount = new BigDecimal(discountValue);
    final BigDecimal quantity = new BigDecimal(productQuantity);

    return switch (discountType) {
      case "fixed_amount" -> price.subtract(discount).multiply(quantity);
      case "percentage" -> {
        final BigDecimal discountPercentage = discount.divide(new BigDecimal(100));
        final BigDecimal discountPrice = price.multiply(discountPercentage);

        yield price.subtract(discountPrice)
          .setScale(2, RoundingMode.HALF_DOWN)
          .multiply(quantity);
      }
      default -> throw new PriceCalculationException("The discount type '" + discountType + "' is not supported");
    };
  }

  public static BigDecimal calculateFinalPrice(String unitPrice, int productQuantity) throws PriceCalculationException {
    final BigDecimal price = new BigDecimal(unitPrice);
    final BigDecimal quantity = new BigDecimal(productQuantity);
    return price.multiply(quantity);
  }
}
