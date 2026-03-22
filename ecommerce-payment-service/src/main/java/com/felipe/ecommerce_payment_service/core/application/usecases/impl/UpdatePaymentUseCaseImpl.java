package com.felipe.ecommerce_payment_service.core.application.usecases.impl;

import com.felipe.ecommerce_payment_service.core.application.exceptions.PaymentNotFoundException;
import com.felipe.ecommerce_payment_service.core.application.gateway.PaymentGateway;
import com.felipe.ecommerce_payment_service.core.application.usecases.UpdatePaymentUseCase;
import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.core.domain.PaymentStatus;

import java.util.UUID;

public class UpdatePaymentUseCaseImpl implements UpdatePaymentUseCase {
  private final PaymentGateway paymentGateway;

  public UpdatePaymentUseCaseImpl(PaymentGateway paymentGateway) {
    this.paymentGateway = paymentGateway;
  }

  @Override
  public Payment execute(UUID orderId, PaymentStatus status) {
    Payment payment = this.paymentGateway.findPaymentByOrderId(orderId)
      .orElseThrow(() -> new PaymentNotFoundException("Pagamento do pedido de id '" + orderId + "' não encontrado"));
    payment.setStatus(status);
    return this.paymentGateway.updatePayment(payment);
  }
}
