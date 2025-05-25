package com.felipe.ecommerce_auth_server.persistence.repositories;

import com.felipe.ecommerce_auth_server.persistence.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, String> {
  Optional<ClientEntity> findByClientId(String clientId);
}
