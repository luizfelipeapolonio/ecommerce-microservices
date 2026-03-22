package com.felipe.ecommerce_payment_service.infrastructure.gateway;

import com.felipe.ecommerce_payment_service.core.application.gateway.PaymentGateway;
import com.felipe.ecommerce_payment_service.core.domain.Payment;
import com.felipe.ecommerce_payment_service.infrastructure.mappers.PaymentEntityMapper;
import com.felipe.ecommerce_payment_service.infrastructure.persistence.entities.PaymentEntity;
import com.felipe.ecommerce_payment_service.infrastructure.persistence.repositories.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PaymentGatewayImpl implements PaymentGateway {
  private final PaymentRepository paymentRepository;
  private final PaymentEntityMapper paymentEntityMapper;

  public PaymentGatewayImpl(PaymentRepository paymentRepository, PaymentEntityMapper paymentEntityMapper) {
    this.paymentRepository = paymentRepository;
    this.paymentEntityMapper = paymentEntityMapper;
  }

  @Override
  public Payment createPayment(Payment payment) {
    PaymentEntity entity = this.paymentEntityMapper.toEntity(payment);
    return this.paymentEntityMapper.toDomain(this.paymentRepository.save(entity));
  }

  @Override
  public Payment updatePayment(Payment payment) {
    PaymentEntity entity = this.paymentEntityMapper.toEntity(payment);
    return this.paymentEntityMapper.toDomain(this.paymentRepository.save(entity));
  }

  @Override
  public Optional<Payment> findPaymentByOrderId(UUID orderId) {
    return this.paymentRepository.findByOrderId(orderId).map(this.paymentEntityMapper::toDomain);
  }
}
