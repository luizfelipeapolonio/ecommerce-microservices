package com.felipe.ecommerce_discount_service.infrastructure.services;

import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.promotion.PromotionEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.CouponRepository;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class DiscountSchedulerService {
  private final TaskScheduler taskScheduler;
  private final PromotionRepository promotionRepository;
  private final CouponRepository couponRepository;
  private final KafkaTemplate<String, Object> kafkaTemplate;

  private static final Logger logger = LoggerFactory.getLogger(DiscountSchedulerService.class);
  private final Map<UUID, ScheduledFuture<?>> scheduledDiscountsToExpire = new ConcurrentHashMap<>();

  public DiscountSchedulerService(TaskScheduler taskScheduler,
                                  PromotionRepository promotionRepository,
                                  CouponRepository couponRepository,
                                  KafkaTemplate<String, Object> kafkaTemplate) {
    this.taskScheduler = taskScheduler;
    this.promotionRepository = promotionRepository;
    this.couponRepository = couponRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  public void schedulePromotionToExpire(PromotionEntity promotion) {
    schedule(promotion.getId(), () -> expirePromotion(promotion), promotion.getEndDate());
    logger.info("schedulePromotionToExpire - Promotion Scheduled: {}", promotion.getName());
    logger.debug("schedulePromotionToExpire - Scheduled Discounts Quantity: {}", this.scheduledDiscountsToExpire.size());
  }

  public void scheduleCouponToExpire(CouponEntity coupon) {
    schedule(coupon.getId(), () -> expireCoupon(coupon), coupon.getEndDate());
    logger.info("scheduleCouponToExpire - Coupon Scheduled: {}", coupon.getCouponCode());
    logger.debug("scheduleCouponToExpire - Scheduled Discounts Quantity: {}", this.scheduledDiscountsToExpire.size());
  }

  public void cancelScheduledPromotion(PromotionEntity promotion) {
    cancelScheduled(promotion.getId(), promotion.getName());
  }

  public void cancelScheduledCoupon(CouponEntity coupon) {
    cancelScheduled(coupon.getId(), coupon.getCouponCode());
  }

  @EventListener(ApplicationReadyEvent.class)
  private void scheduleDiscountsOnStartUp() {
    logger.info("=== Scheduling Discounts to Expire ===");

    List<PromotionEntity> promotions = this.promotionRepository.findAllByEndDateAfterNowAndIsActiveTrue();
    logger.info("Found promotions to schedule -> quantity: {}", promotions.size());
    List<CouponEntity> coupons = this.couponRepository.findAllByEndDateAfterNowAndIsActiveTrue();
    logger.info("Found coupons to schedule -> quantity: {}", coupons.size());

    promotions.forEach(this::schedulePromotionToExpire);
    coupons.forEach(this::scheduleCouponToExpire);
    logger.info("Scheduled Discounts to Expire Quantity: {}", this.scheduledDiscountsToExpire.size());
  }

  private void schedule(UUID discountId, Runnable task, LocalDateTime startTime) {
    Instant instantStartTime = startTime.atZone(ZoneId.systemDefault()).toInstant();
    ScheduledFuture<?> future = this.taskScheduler.schedule(task, instantStartTime);
    this.scheduledDiscountsToExpire.put(discountId, future);
  }

  private void cancelScheduled(UUID discountId, String discountName) {
    ScheduledFuture<?> scheduledPromotion = this.scheduledDiscountsToExpire.get(discountId);

    if (scheduledPromotion != null) {
      scheduledPromotion.cancel(true);
      this.scheduledDiscountsToExpire.remove(discountId);
      logger.info("cancelScheduledDiscount - Cancelling Scheduled Discount: {}", discountName);
      logger.debug("cancelScheduledDiscount - Scheduled Discounts Quantity: {}", this.scheduledDiscountsToExpire.size());
    }
  }

  private void expirePromotion(PromotionEntity promotion) {
    this.promotionRepository.findById(promotion.getId()).ifPresent(foundPromotion -> {
      final PromotionEntity expiredPromotion = PromotionEntity.mutate(foundPromotion)
        .isActive(false)
        .build();
      this.promotionRepository.save(expiredPromotion);
      logger.info("expirePromotion - Expiring Promotion: {}", expiredPromotion.getName());

      // Sending the expired promotion to the Kafka topic
      this.kafkaTemplate.send("expired-promotion", new ExpiredPromotionKafkaDTO(expiredPromotion.getId().toString()))
        .whenComplete((result, ex) -> {
          if (ex != null) {
            throw new KafkaProducerException(
              result.getProducerRecord(),
              "Something went wrong while trying to post to a kafka topic",
              ex
            );
          }
          logger.info(
            "Promotion: {} was posted on topic \"{}\" successfully",
            expiredPromotion.getId(),
            result.getRecordMetadata().topic()
          );
        });
      this.scheduledDiscountsToExpire.remove(promotion.getId());
    });
  }

  private void expireCoupon(CouponEntity coupon) {
    this.couponRepository.findById(coupon.getId()).ifPresent(foundCoupon -> {
      foundCoupon.setIsActive(false);
      this.couponRepository.save(foundCoupon);
      logger.info("expireCoupon - Expiring Coupon: {}", foundCoupon.getCouponCode());
      this.scheduledDiscountsToExpire.remove(coupon.getId());
    });
  }
}
