package com.felipe.ecommerce_payment_service.core.application.gateway;

import com.felipe.ecommerce_payment_service.core.domain.Payment;

public interface PaymentGateway {
  Payment createPayment(Payment payment);
}
