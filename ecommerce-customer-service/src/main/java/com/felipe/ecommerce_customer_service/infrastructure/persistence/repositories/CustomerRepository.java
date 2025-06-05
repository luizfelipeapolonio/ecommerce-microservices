package com.felipe.ecommerce_customer_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_customer_service.infrastructure.persistence.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository extends JpaRepository<CustomerEntity, UUID> {
  Optional<CustomerEntity> findByEmail(String email);
}
