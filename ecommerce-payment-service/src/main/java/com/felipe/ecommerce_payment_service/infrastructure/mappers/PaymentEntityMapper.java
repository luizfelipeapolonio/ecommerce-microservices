package com.felipe.ecommerce_payment_service.infrastructure.mappers;

import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.infrastructure.persistence.entities.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityMapper {
  public Payment toDomain(PaymentEntity entity) {
    return new Payment()
      .id(entity.getId())
      .orderId(entity.getOrderId())
      .orderAmount(entity.getOrderAmount())
      .customerId(entity.getCustomerId())
      .stripeCustomerId(entity.getStripeCustomerId())
      .sagaId(entity.getSagaId())
      .transactionId(entity.getTransactionId())
      .checkoutId(entity.getCheckoutId())
      .status(entity.getStatus())
      .createdAt(entity.getCreatedAt())
      .updatedAt(entity.getUpdatedAt());
  }

  public PaymentEntity toEntity(Payment payment) {
    return new PaymentEntity()
      .id(payment.getId())
      .orderId(payment.getOrderId())
      .orderAmount(payment.getOrderAmount())
      .customerId(payment.getCustomerId())
      .stripeCustomerId(payment.getStripeCustomerId())
      .sagaId(payment.getSagaId())
      .transactionId(payment.getTransactionId())
      .checkoutId(payment.getCheckoutId())
      .status(payment.getStatus())
      .createdAt(payment.getCreatedAt())
      .updatedAt(payment.getUpdatedAt());
  }
}
