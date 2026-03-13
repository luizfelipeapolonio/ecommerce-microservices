package com.felipe.ecommerce_order_service.core.application.dtos;

import com.felipe.ecommerce_order_service.core.domain.enums.OrderStatus;

import java.util.List;

public record UpdateOrderDTO(
  OrderStatus status,
  String checkoutUrl,
  String invoiceUrl,
  String orderPrice,
  List<UpdateProductDTO> products
) {}
