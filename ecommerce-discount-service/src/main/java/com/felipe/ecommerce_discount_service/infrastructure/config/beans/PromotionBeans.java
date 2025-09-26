package com.felipe.ecommerce_discount_service.infrastructure.config.beans;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.CreatePromotionUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionBeans {
  private final PromotionGateway promotionGateway;

  public PromotionBeans(PromotionGateway promotionGateway) {
    this.promotionGateway = promotionGateway;
  }

  @Bean
  public CreatePromotionUseCase createPromotionUseCase() {
    return new CreatePromotionUseCaseImpl(promotionGateway);
  }
}
