package com.felipe.ecommerce_discount_service.infrastructure.config.beans;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.DeletePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllActiveOrInactivePromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsByDiscountTypeUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetAllPromotionsByDiscountTypeUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetPromotionByIdUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.UpdatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.CreatePromotionUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.DeletePromotionUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetAllActiveOrInactivePromotionsUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetAllPromotionsUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetPromotionByIdUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.UpdatePromotionUseCaseImpl;
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

  @Bean
  public UpdatePromotionUseCase updatePromotionUseCase() {
    return new UpdatePromotionUseCaseImpl(promotionGateway);
  }

  @Bean
  public GetPromotionByIdUseCase getPromotionByIdUseCase() {
    return new GetPromotionByIdUseCaseImpl(promotionGateway);
  }

  @Bean
  public GetAllPromotionsUseCase getAllPromotionsUseCase() {
    return new GetAllPromotionsUseCaseImpl(promotionGateway);
  }

  @Bean
  public GetAllActiveOrInactivePromotionsUseCase getAllActiveOrInactivePromotionsUseCase() {
    return new GetAllActiveOrInactivePromotionsUseCaseImpl(promotionGateway);
  }

  @Bean
  public GetAllPromotionsByDiscountTypeUseCase getAllPromotionsByDiscountTypeUseCase() {
    return new GetAllPromotionsByDiscountTypeUseCaseImpl(promotionGateway);
  }
}
