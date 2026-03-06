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
  public Payment execute(UUID orderId, String orderAmount, UUID customerId, String checkoutId) {
    Payment payment = new Payment()
      .orderId(orderId)
      .orderAmount(new BigDecimal(orderAmount))
      .customerId(customerId)
      .checkoutId(checkoutId);
    return this.paymentGateway.createPayment(payment);
  }
}
