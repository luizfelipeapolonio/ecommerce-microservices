package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.EndDateDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.UpdatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.UpdatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class UpdatePromotionUseCaseImpl implements UpdatePromotionUseCase {
  private final PromotionGateway promotionGateway;

  public UpdatePromotionUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public Promotion execute(UUID promotionId, UpdatePromotionDTO promotionDTO) {
    final Promotion promotion = this.promotionGateway.findActivePromotionById(promotionId)
      .map(foundPromotion -> {
        final Promotion.Builder promotionBuilder = Promotion.mutate(foundPromotion);

        if(promotionDTO.name() != null) {
          promotionBuilder.name(promotionDTO.name());
        }
        if(promotionDTO.description() != null) {
          promotionBuilder.description(promotionDTO.description());
        }
        if(promotionDTO.endDate() != null) {
          final LocalDateTime updatedEndDate = convertEndDate(promotionDTO.endDate());

          if(updatedEndDate.isBefore(LocalDateTime.now())) {
            throw new InvalidEndDateException(updatedEndDate.toString());
          }
          promotionBuilder.endDate(updatedEndDate);
        }

        return promotionBuilder.build();
      })
      .orElseThrow(() -> new DataNotFoundException("Promoção de id: '" + promotionId + "' não encontrada"));

    return this.promotionGateway.updatePromotion(promotion);
  }

  private LocalDateTime convertEndDate(EndDateDTO endDate) {
    final LocalDate date = LocalDate.of(endDate.year(), endDate.month(), endDate.dayOfMonth());
    final LocalTime time = LocalTime.of(endDate.hour(), endDate.minute(), endDate.second());
    return LocalDateTime.of(date, time);
  }
}
