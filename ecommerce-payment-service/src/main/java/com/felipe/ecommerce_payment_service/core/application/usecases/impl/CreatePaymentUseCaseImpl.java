package com.felipe.ecommerce_payment_service.core.application.usecases.impl;

import com.felipe.ecommerce_payment_service.core.application.dtos.CreatePaymentDTO;
import com.felipe.ecommerce_payment_service.core.application.gateway.PaymentGateway;
import com.felipe.ecommerce_payment_service.core.application.usecases.CreatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;

import java.math.BigDecimal;

public class CreatePaymentUseCaseImpl implements CreatePaymentUseCase {
  private final PaymentGateway paymentGateway;

  public CreatePaymentUseCaseImpl(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  @Override
  public Payment execute(CreatePaymentDTO paymentDTO) {
    Payment payment = new Payment()
      .orderId(paymentDTO.orderId())
      .sagaId(paymentDTO.sagaId())
      .transactionId(paymentDTO.transactionId())
      .orderAmount(new BigDecimal(paymentDTO.orderAmount()))
      .customerId(paymentDTO.customerId())
      .customerEmail(paymentDTO.customerEmail())
      .stripeCustomerId(paymentDTO.stripeCustomerId())
      .checkoutId(paymentDTO.checkoutId());
    return this.paymentGateway.createPayment(payment);
  }
}
