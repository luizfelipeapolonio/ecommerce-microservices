package com.felipe.ecommerce_order_service.core.application.gateway;

import java.util.Map;
import java.util.UUID;

public interface CouponGateway {
  void checkIfCouponIsValid(String couponCode);
}
