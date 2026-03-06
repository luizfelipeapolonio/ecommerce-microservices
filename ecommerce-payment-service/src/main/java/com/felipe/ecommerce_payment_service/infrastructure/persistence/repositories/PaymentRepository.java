package com.felipe.ecommerce_payment_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_payment_service.infrastructure.persistence.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
}
