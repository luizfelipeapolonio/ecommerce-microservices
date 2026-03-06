package com.felipe.ecommerce_payment_service.core.application.usecases;

import com.felipe.ecommerce_payment_service.core.domain.Payment;

import java.util.UUID;

public interface CreatePaymentUseCase {
  Payment execute(UUID orderId, String orderAmount, UUID customerId, String checkoutId);
}
