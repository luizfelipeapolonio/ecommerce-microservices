package com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.CreatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.EndDateDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.PromotionAppliesToDTO;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionAppliesTarget;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.core.domain.enums.PromotionScope;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CreatePromotionUseCaseImpl implements CreatePromotionUseCase {
  private final PromotionGateway promotionGateway;

  public CreatePromotionUseCaseImpl(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Override
  public Optional<Promotion> execute(CreatePromotionDTO promotionDTO) {
    final LocalDateTime endDate = convertEndDate(promotionDTO.endDate());
    if(endDate.isBefore(LocalDateTime.now())) {
      throw new InvalidEndDateException(endDate.toString());
    }

    final Promotion.Builder promotionBuilder = Promotion.builder();
    final DiscountType discountType = DiscountType.of(promotionDTO.discountType());

    // The promotion minimum price for fixed_amount should be the discount value plus 10
    if(discountType == DiscountType.FIXED_AMOUNT) {
      final BigDecimal discountValue = new BigDecimal(promotionDTO.discountValue());
      promotionBuilder.minimumPrice(discountValue.add(new BigDecimal("10.00")));
    } else {
      promotionBuilder.minimumPrice(new BigDecimal(promotionDTO.minimumPrice()));
    }

    promotionBuilder.name(promotionDTO.name());
    promotionBuilder.description(promotionDTO.description());
    promotionBuilder.scope(PromotionScope.of(promotionDTO.scope()));
    promotionBuilder.discountType(discountType);
    promotionBuilder.discountValue(promotionDTO.discountValue());
    promotionBuilder.endDate(endDate);
    promotionBuilder.promotionApplies(createPromotionApplies(promotionDTO.promotionApplies()));

    return this.promotionGateway.createPromotion(promotionBuilder.build());
  }

  private LocalDateTime convertEndDate(EndDateDTO endDate) {
    final LocalDate date = LocalDate.of(endDate.year(), Month.of(endDate.month()), endDate.dayOfMonth());
    final LocalTime time = LocalTime.of(endDate.hour(), endDate.minute(), endDate.second());
    return LocalDateTime.of(date, time);
  }

  private List<PromotionAppliesTo> createPromotionApplies(List<? extends PromotionAppliesToDTO> promotionAppliesDTO) {
    final List<PromotionAppliesTo> promotionApplies = new ArrayList<>(promotionAppliesDTO.size());
    promotionAppliesDTO.forEach(promotionAppliesTo -> {
      final PromotionAppliesTo appliesTo = new PromotionAppliesTo();
      appliesTo.setTarget(PromotionAppliesTarget.of(promotionAppliesTo.target()));
      appliesTo.setTargetId(promotionAppliesTo.targetId());
      promotionApplies.add(appliesTo);
    });
    return promotionApplies;
  }
}
