package com.felipe.ecommerce_payment_service.core.application.gateway;

import com.felipe.ecommerce_payment_service.core.domain.Payment;

import java.util.Optional;
import java.util.UUID;

public interface PaymentGateway {
  Payment createPayment(Payment payment);
  Payment updatePayment(Payment payment);
  Optional<Payment> findPaymentByOrderId(UUID orderId);
}
