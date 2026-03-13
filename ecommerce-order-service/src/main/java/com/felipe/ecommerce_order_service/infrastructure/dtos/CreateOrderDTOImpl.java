package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;

import java.util.List;

public record CreateOrderDTOImpl(
  List<CreateOrderProductDTOImpl> products,
  String couponCode
) implements CreateOrderDTO {}
