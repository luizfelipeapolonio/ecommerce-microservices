package com.felipe.ecommerce_shipping_service.infrastructure.utils;

import com.felipe.ecommerce_shipping_service.infrastructure.exceptions.ShippingFeeNotDefinedException;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Distance;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.ShippingFeeEntity;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.entities.shipping_fee.Weight;
import com.felipe.ecommerce_shipping_service.infrastructure.persistence.repositories.ShippingFeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Component
public class ShippingCalculator {
  private final ShippingFeeRepository shippingFeeRepository;
  private static final Logger logger = LoggerFactory.getLogger(ShippingCalculator.class);

  public ShippingCalculator(ShippingFeeRepository shippingFeeRepository) {
    this.shippingFeeRepository = shippingFeeRepository;
  }

  public String calculateShippingFee() {
    Random random = new Random();
    Distance distance = getRandomDistance(random);
    Weight weight = getRandomWeight(random);

    ShippingFeeEntity shippingFee = this.shippingFeeRepository.findByDistanceAndWeight(distance, weight)
      .orElseThrow(() -> new ShippingFeeNotDefinedException(distance, weight));

    logger.info("Calculating shipping fee for Distance: {} - Weight: {} - Fee: R$ {}",
      distance.name(), weight.name(), shippingFee.getPrice().toPlainString());
    return shippingFee.getPrice().toPlainString();
  }

  private Distance getRandomDistance(Random randomInstance) {
    List<Integer> ids = Stream.of(Distance.values()).map(Distance::id).toList();
    Integer minId = Collections.min(ids);
    Integer maxId = Collections.max(ids);

    int distanceId = randomInstance.nextInt(minId, (maxId + 1));
    return Distance.of(distanceId);
  }

  private Weight getRandomWeight(Random randomInstance) {
    List<Integer> ids = Stream.of(Weight.values()).map(Weight::id).toList();
    Integer minId = Collections.min(ids);
    Integer maxId = Collections.max(ids);

    int weightId = randomInstance.nextInt(minId, (maxId + 1));
    return Weight.of(weightId);
  }
}
