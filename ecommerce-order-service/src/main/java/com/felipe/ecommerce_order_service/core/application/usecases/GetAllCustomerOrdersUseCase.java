package com.felipe.ecommerce_order_service.core.application.usecases;

import com.felipe.ecommerce_order_service.core.domain.Order;

import java.util.List;

public interface GetAllCustomerOrdersUseCase {
  List<Order> execute(String customerEmail);
}
