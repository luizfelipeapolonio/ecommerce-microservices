package com.felipe.ecommerce_discount_service.core.application.dtos.promotion;

public interface EndDateDTO {
  int dayOfMonth();
  int month();
  int year();
  int hour();
  int minute();
  int second();
}
