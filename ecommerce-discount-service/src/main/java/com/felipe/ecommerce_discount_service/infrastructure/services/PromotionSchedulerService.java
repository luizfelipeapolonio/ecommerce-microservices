package com.felipe.ecommerce_discount_service.infrastructure.services;

import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.PromotionRepository;
import com.felipe.kafka.ExpiredPromotionKafkaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class PromotionSchedulerService {
  private final TaskScheduler taskScheduler;
  private final PromotionRepository promotionRepository;
  private final KafkaTemplate<String, ExpiredPromotionKafkaDTO> kafkaTemplate;

  private final Logger logger = LoggerFactory.getLogger(PromotionSchedulerService.class);
  private final Map<UUID, ScheduledFuture<?>> scheduledPromotionsToExpire = new ConcurrentHashMap<>();

  public PromotionSchedulerService(TaskScheduler taskScheduler, PromotionRepository promotionRepository,
                                   KafkaTemplate<String, ExpiredPromotionKafkaDTO> kafkaTemplate) {
    this.taskScheduler = taskScheduler;
    this.promotionRepository = promotionRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void schedulePromotionToExpire(PromotionEntity promotion) {
    final ScheduledFuture<?> future = this.taskScheduler.schedule(
      () -> this.expirePromotion(promotion),
      promotion.getEndDate().atZone(ZoneId.systemDefault()).toInstant()
    );
    this.scheduledPromotionsToExpire.put(promotion.getId(), future);
    this.logger.info("schedulePromotionToExpire - Promotion Scheduled: {}", promotion.getName());
    this.logger.debug("schedulePromotionToExpire - Scheduled Promotions Quantity: {}", this.scheduledPromotionsToExpire.size());
  }

  public void cancelScheduledPromotion(PromotionEntity promotion) {
    final ScheduledFuture<?> scheduledPromotion = this.scheduledPromotionsToExpire.get(promotion.getId());

    if(scheduledPromotion != null) {
      scheduledPromotion.cancel(true);
      this.scheduledPromotionsToExpire.remove(promotion.getId());
      this.logger.info("cancelScheduledPromotion - Cancelling Scheduled Promotion: {}", promotion.getName());
      this.logger.debug("cancelScheduledPromotion - Scheduled Promotions Quantity: {}", this.scheduledPromotionsToExpire.size());
    }
  }

  @EventListener(ApplicationReadyEvent.class)
  private void schedulePromotionsOnStartUp() {
    this.logger.info("=== Scheduling Promotions to Expire ===");
    this.promotionRepository.findAllByEndDateAfterNowAndIsActiveTrue().forEach(this::schedulePromotionToExpire);
    this.logger.info("Scheduled Promotions to Expire Quantity: {}", this.scheduledPromotionsToExpire.size());
  }

  private void expirePromotion(PromotionEntity promotion) {
    this.promotionRepository.findById(promotion.getId()).ifPresent(foundPromotion -> {
      final PromotionEntity expiredPromotion = PromotionEntity.mutate(foundPromotion)
        .isActive(false)
        .build();
      this.promotionRepository.save(expiredPromotion);
      this.logger.info("expirePromotion - Expiring Promotion: {}", expiredPromotion.getName());

      // Sending the expired promotion to the Kafka topic
      this.kafkaTemplate.send("expired-promotion", new ExpiredPromotionKafkaDTO(expiredPromotion.getId().toString()))
        .whenComplete((result, ex) -> {
          if(ex != null) {
            throw new KafkaProducerException(
              result.getProducerRecord(),
              "Something went wrong while trying to post to a kafka topic",
              ex
            );
          }
          this.logger.info(
            "Promotion: {} was posted on topic \"{}\" successfully",
            result.getProducerRecord().value().promotionId(),
            result.getRecordMetadata().topic()
          );
        });
      this.scheduledPromotionsToExpire.remove(promotion.getId());
    });
  }
}
