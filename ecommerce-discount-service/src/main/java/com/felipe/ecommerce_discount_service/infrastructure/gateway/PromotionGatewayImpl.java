package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.external.InventoryService;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.PromotionEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.PromotionEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.PromotionRepository;
import com.felipe.response.ResponsePayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PromotionGatewayImpl implements PromotionGateway {
  private final PromotionRepository promotionRepository;
  private final PromotionEntityMapper promotionEntityMapper;
  private final InventoryService inventoryService;
  private final Logger logger = LoggerFactory.getLogger(PromotionGatewayImpl.class);

  public PromotionGatewayImpl(PromotionRepository promotionRepository, PromotionEntityMapper promotionEntityMapper,
                              InventoryService inventoryService) {
    this.promotionRepository = promotionRepository;
    this.promotionEntityMapper = promotionEntityMapper;
    this.inventoryService = inventoryService;
  }

  @Override
  public Optional<Promotion> createPromotion(Promotion promotion) {
    final PromotionEntity promotionEntity = this.promotionEntityMapper.toEntity(promotion);
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

    return Optional.of(this.promotionEntityMapper.toDomain(savedPromotion));
  }
}
