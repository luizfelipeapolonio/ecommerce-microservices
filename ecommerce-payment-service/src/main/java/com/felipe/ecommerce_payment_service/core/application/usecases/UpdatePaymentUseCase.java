package com.felipe.ecommerce_payment_service.core.application.usecases;

import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.core.domain.PaymentStatus;

import java.util.UUID;

public interface UpdatePaymentUseCase {
  Payment execute(UUID orderId, PaymentStatus status);
}
