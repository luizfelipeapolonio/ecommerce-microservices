package com.felipe.ecommerce_payment_service.core.application.dtos;

import java.util.UUID;

public record CreatePaymentDTO(UUID orderId,
                               UUID sagaId,
                               UUID transactionId,
                               String orderAmount,
                               UUID customerId,
                               String customerEmail,
                               String stripeCustomerId,
                               String checkoutId) {}
