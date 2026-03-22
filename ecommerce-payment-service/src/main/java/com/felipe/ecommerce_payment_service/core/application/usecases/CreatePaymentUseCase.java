package com.felipe.ecommerce_payment_service.core.application.usecases;

import com.felipe.ecommerce_payment_service.core.domain.Payment;

import java.util.UUID;

public interface CreatePaymentUseCase {
  Payment execute(UUID orderId, UUID sagaId, UUID transactionId, String orderAmount, UUID customerId, String stripeCustomerId, String checkoutId);
}
