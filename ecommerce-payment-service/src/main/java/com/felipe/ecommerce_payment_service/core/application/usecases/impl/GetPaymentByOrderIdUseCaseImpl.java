package com.felipe.ecommerce_payment_service.core.application.usecases.impl;

import com.felipe.ecommerce_payment_service.core.application.exceptions.PaymentNotFoundException;
import com.felipe.ecommerce_payment_service.core.application.gateway.PaymentGateway;
import com.felipe.ecommerce_payment_service.core.application.usecases.GetPaymentByOrderIdUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;

import java.util.UUID;

public class GetPaymentByOrderIdUseCaseImpl implements GetPaymentByOrderIdUseCase {
  private final PaymentGateway paymentGateway;

  public GetPaymentByOrderIdUseCaseImpl(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  @Override
  public Payment execute(UUID orderId) {
    return this.paymentGateway.findPaymentByOrderId(orderId)
      .orElseThrow(() -> new PaymentNotFoundException("Pagamento do pedido de id '" + orderId + "' não encontrado"));
  }
}
