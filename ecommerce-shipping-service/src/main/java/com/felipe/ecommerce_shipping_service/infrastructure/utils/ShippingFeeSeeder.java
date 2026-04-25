package com.felipe.ecommerce_shipping_service.infrastructure.utils;

import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Distance;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.ShippingFeeEntity;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Weight;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories.ShippingFeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class ShippingFeeSeeder {
  private static final Logger logger = LoggerFactory.getLogger(ShippingFeeSeeder.class);

  @Bean
  CommandLineRunner feesInitializer(ShippingFeeRepository shippingFeeRepository) {
    return args -> {
      List<ShippingFeeEntity> existingFees = shippingFeeRepository.findAll();
      if (!existingFees.isEmpty()) return;

      List<ShippingFeeEntity> fees = List.of(
        new ShippingFeeEntity(Distance.NEAR, Weight.LIGHT, new BigDecimal("15.00")),
        new ShippingFeeEntity(Distance.NEAR, Weight.MODERATE, new BigDecimal("25.00")),
        new ShippingFeeEntity(Distance.NEAR, Weight.HEAVY, new BigDecimal("35.00")),
        new ShippingFeeEntity(Distance.NEAR, Weight.VERY_HEAVY, new BigDecimal("45.00")),

        new ShippingFeeEntity(Distance.MEDIUM, Weight.LIGHT, new BigDecimal("25.00")),
        new ShippingFeeEntity(Distance.MEDIUM, Weight.MODERATE, new BigDecimal("35.00")),
        new ShippingFeeEntity(Distance.MEDIUM, Weight.HEAVY, new BigDecimal("45.00")),
        new ShippingFeeEntity(Distance.MEDIUM, Weight.VERY_HEAVY, new BigDecimal("55.00")),

        new ShippingFeeEntity(Distance.FAR, Weight.LIGHT, new BigDecimal("35.00")),
        new ShippingFeeEntity(Distance.FAR, Weight.MODERATE, new BigDecimal("45.00")),
        new ShippingFeeEntity(Distance.FAR, Weight.HEAVY, new BigDecimal("55.00")),
        new ShippingFeeEntity(Distance.FAR, Weight.VERY_HEAVY, new BigDecimal("65.00")),

        new ShippingFeeEntity(Distance.VERY_FAR, Weight.LIGHT, new BigDecimal("45.00")),
        new ShippingFeeEntity(Distance.VERY_FAR, Weight.MODERATE, new BigDecimal("55.00")),
        new ShippingFeeEntity(Distance.VERY_FAR, Weight.HEAVY, new BigDecimal("65.00")),
        new ShippingFeeEntity(Distance.VERY_FAR, Weight.VERY_HEAVY, new BigDecimal("75.00"))
      );
      shippingFeeRepository.saveAll(fees);
      logger.info("ShippingFeeSeeder - Shipping fees inserted on database successfully");
    };
  }
}
