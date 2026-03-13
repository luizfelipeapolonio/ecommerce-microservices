package com.felipe.ecommerce_order_service.core.application.dtos;

import java.util.List;

public interface CreateOrderDTO {
  List<? extends CreateOrderProductDTO> products();
  String couponCode();
}
