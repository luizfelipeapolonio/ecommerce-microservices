package com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories;

import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Distance;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.ShippingFeeEntity;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Weight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingFeeRepository extends JpaRepository<ShippingFeeEntity, Long> {
  Optional<ShippingFeeEntity> findByDistanceAndWeight(Distance distance, Weight weight);
}
