package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.external.InventoryService;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.PromotionEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.PromotionRepository;
import com.felipe.ecommerce_discount_service.infrastructure.services.PromotionSchedulerService;
import com.felipe.kafka.ExpiredPromotionKafkaDTO;
import com.felipe.response.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Component
public class PromotionGatewayImpl implements PromotionGateway {
  private final PromotionRepository promotionRepository;
  private final PromotionEntityMapper promotionEntityMapper;
  private final InventoryService inventoryService;
  private final PromotionSchedulerService promotionSchedulerService;
  private final KafkaTemplate<String, ExpiredPromotionKafkaDTO> kafkaTemplate;

  private final Logger logger = LoggerFactory.getLogger(PromotionGatewayImpl.class);

  public PromotionGatewayImpl(PromotionRepository promotionRepository,
                              PromotionEntityMapper promotionEntityMapper,
                              InventoryService inventoryService,
                              PromotionSchedulerService promotionSchedulerService,
                              KafkaTemplate<String, ExpiredPromotionKafkaDTO> kafkaTemplate) {
    this.promotionRepository = promotionRepository;
    this.promotionEntityMapper = promotionEntityMapper;
    this.inventoryService = inventoryService;
    this.promotionSchedulerService = promotionSchedulerService;
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public Optional<Promotion> createPromotion(Promotion promotion) {
    final Promotion promotionWithId = Promotion.mutate(promotion).id(UUID.randomUUID()).build();
    final PromotionEntity promotionEntity = this.promotionEntityMapper.toEntity(promotionWithId);
    final List<PromotionAppliesToDTOImpl> promotionAppliesDTO = promotionEntity.getPromotionApplies()
      .stream()
      .map(PromotionAppliesToDTOImpl::new)
      .toList();

    final var promotionRequest = new InventoryService.PromotionRequest(promotionEntity, promotionAppliesDTO);
    final ResponsePayload<Map<String, Integer>> appliedPromotionResponse = this.inventoryService.applyPromotion(promotionRequest);
    this.logger.info("Applied promotion response message: {}", appliedPromotionResponse.getMessage());

    if(appliedPromotionResponse.getPayload().get("appliedPromotionQuantity") == 0) {
      return Optional.empty();
    }

    final PromotionEntity savedPromotion = this.promotionRepository.save(promotionEntity);
    this.promotionSchedulerService.schedulePromotionToExpire(savedPromotion);

    return Optional.of(this.promotionEntityMapper.toDomain(savedPromotion));
  }

  @Override
  public Optional<Promotion> findPromotionById(UUID promotionId) {
    return this.promotionRepository.findById(promotionId)
      .map(this.promotionEntityMapper::toDomain);
  }

  @Override
  public Promotion deletePromotion(Promotion promotion) {
    final PromotionEntity promotionEntity = this.promotionEntityMapper.toEntity(promotion);
    this.promotionRepository.delete(promotionEntity);
    this.promotionSchedulerService.cancelScheduledPromotion(promotionEntity);

    if(promotion.isActive()) {
      this.kafkaTemplate.send("expired-promotion", new ExpiredPromotionKafkaDTO(promotion.getId().toString()));
      this.logger.info("cancelScheduledPromotion - Promotion \"{}\" to remove posted on topic", promotion.getId());
    }
    return promotion;
  }

  @Override
  public Optional<Promotion> findActivePromotionById(UUID promotionId) {
    return this.promotionRepository.findByIdAndIsActiveTrue(promotionId)
      .map(this.promotionEntityMapper::toDomain);
  }

  @Override
  public Promotion updatePromotion(Promotion promotion) {
    final PromotionEntity entity = this.promotionEntityMapper.toEntity(promotion);
    this.promotionSchedulerService.cancelScheduledPromotion(entity);

    final PromotionEntity updatedPromotion = this.promotionRepository.save(entity);
    this.promotionSchedulerService.schedulePromotionToExpire(updatedPromotion);
    return this.promotionEntityMapper.toDomain(updatedPromotion);
  }

  @Override
  public List<Promotion> findAllPromotions() {
    return this.promotionRepository.findAll()
      .stream()
      .map(this.promotionEntityMapper::toDomain)
      .toList();
  }

  @Override
  public List<Promotion> findAllActiveOrInactivePromotions(boolean isActive) {
    return this.promotionRepository.findAllByIsActive(isActive)
      .stream()
      .map(this.promotionEntityMapper::toDomain)
      .toList();
  }

  @Override
  public List<Promotion> findAllPromotionsByDiscountType(DiscountType discountType) {
    return this.promotionRepository.findAllByDiscountType(discountType.getText())
      .stream()
      .map(this.promotionEntityMapper::toDomain)
      .toList();
  }
}
