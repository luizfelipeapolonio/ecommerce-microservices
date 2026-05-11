package com.felipe.ecommerce_payment_service.core.application.usecases;

import com.felipe.ecommerce_payment_service.core.application.dtos.CreatePaymentDTO;
import com.felipe.ecommerce_payment_service.core.domain.Payment;

public interface CreatePaymentUseCase {
  Payment execute(CreatePaymentDTO paymentDTO);
}
