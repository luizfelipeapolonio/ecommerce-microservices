package com.felipe.ecommerce_order_service.infrastructure.dtos;

import com.felipe.ecommerce_order_service.core.application.dtos.CreateOrderDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record CreateOrderDTOImpl(
  @Schema(type = "array")
  List<CreateOrderProductDTOImpl> products,
  @Schema(type = "string", example = "COUPON20OFF")
  String couponCode
) implements CreateOrderDTO {}
