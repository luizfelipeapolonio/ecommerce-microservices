package com.felipe.ecommerce_mail_service.persistence.repositories;

import com.felipe.ecommerce_mail_service.persistence.entities.MailTo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface MailToRepository extends JpaRepository<MailTo, Long> {
  Optional<MailTo> findByOrderId(UUID orderId);
}
