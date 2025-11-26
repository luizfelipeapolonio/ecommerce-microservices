package com.felipe.ecommerce_inventory_service.infrastructure.external;

import com.felipe.ecommerce_inventory_service.infrastructure.persistence.entities.ProductEntity;
import com.felipe.ecommerce_inventory_service.infrastructure.persistence.repositories.ProductRepository;
import com.felipe.kafka.ExpiredPromotionKafkaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
  private final ProductRepository productRepository;
  private final Logger logger = LoggerFactory.getLogger(KafkaService.class);

  public KafkaService(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @KafkaListener(topics = "expired-promotion", groupId = "promotion-group")
  void removeExpiredPromotionFromProducts(ExpiredPromotionKafkaDTO expiredPromotionDTO) {
    this.logger.info("=== Removing expired promotions ===");
    this.productRepository.findAllByPromotionId(expiredPromotionDTO.promotionId())
      .forEach(product -> {
        this.logger.info("Removing promotion \"{}\" from product \"{}\"", expiredPromotionDTO.promotionId(), product.getName());
        final ProductEntity updatedEntity = ProductEntity.mutate(product)
          .withDiscount(false)
          .promotionId(null)
          .discountType(null)
          .discountValue(null)
          .build();

        this.productRepository.save(updatedEntity);
      });
  }
}
