package com.felipe.ecommerce_payment_service.core.application.usecases.impl;

import com.felipe.ecommerce_payment_service.core.application.gateway.PaymentGateway;
import com.felipe.ecommerce_payment_service.core.application.usecases.CreatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;

import java.math.BigDecimal;
import java.util.UUID;

public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {
  private final PaymentGateway paymentGateway;

  public CreatePaymentUseCaseImpl(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  @Override
  public Payment execute(UUID orderId, UUID sagaId, UUID transactionId, String orderAmount, UUID customerId, String stripeCustomerId, String checkoutId) {
    Payment payment = new Payment()
      .orderId(orderId)
      .sagaId(sagaId)
      .transactionId(transactionId)
      .orderAmount(new BigDecimal(orderAmount))
      .customerId(customerId)
      .stripeCustomerId(stripeCustomerId)
      .checkoutId(checkoutId);
    return this.paymentGateway.createPayment(payment);
  }
}
