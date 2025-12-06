package com.felipe.ecommerce_discount_service.infrastructure.config.beans;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.DeletePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.CreatePromotionUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.DeletePromotionUseCaseImpl;
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

  @Bean
  public DeletePromotionUseCase deletePromotionUseCase() {
    return new DeletePromotionUseCaseImpl(promotionGateway);
  }
}
