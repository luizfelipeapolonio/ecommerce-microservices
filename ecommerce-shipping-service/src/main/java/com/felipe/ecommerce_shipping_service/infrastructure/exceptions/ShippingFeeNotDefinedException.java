package com.felipe.ecommerce_shipping_service.infrastructure.exceptions;

import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Distance;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Weight;

public class ShippingFeeNotDefinedException extends RuntimeException {
  public ShippingFeeNotDefinedException(Distance distance, Weight weight) {
    super("The shipping fee with distance '" + distance.name() + "' and weight '" + weight.name() + "' is not defined");
  }
}
