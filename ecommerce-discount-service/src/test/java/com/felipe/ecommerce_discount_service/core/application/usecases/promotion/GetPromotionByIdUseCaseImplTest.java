package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetPromotionByIdUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ExtendWith(MockitoExtension.class)
public class GetPromotionByIdUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private GetPromotionByIdUseCaseImpl getPromotionByIdUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getPromotionByIdUseCase =  new GetPromotionByIdUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getPromotionByIdSuccess - Should successfully return a promotion")
  void getPromotionByIdSuccess() {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();

    when(this.promotionGateway.findPromotionById(promotion.getId())).thenReturn(Optional.of(promotion));

    Promotion foundPromotion = this.getPromotionByIdUseCase.execute(promotion.getId());

    assertThat(foundPromotion.getId()).isEqualTo(promotion.getId());
    assertThat(foundPromotion.getName()).isEqualTo(promotion.getName());
    assertThat(foundPromotion.getDescription()).isEqualTo(promotion.getDescription());
    assertThat(foundPromotion.getDiscountType()).isEqualTo(promotion.getDiscountType());
    assertThat(foundPromotion.getDiscountValue()).isEqualTo(promotion.getDiscountValue());
    assertThat(foundPromotion.getMinimumPrice().toString()).isEqualTo(promotion.getMinimumPrice().toString());
    assertThat(foundPromotion.getScope()).isEqualTo(promotion.getScope());
    assertThat(foundPromotion.isActive()).isEqualTo(promotion.isActive());
    assertThat(foundPromotion.getEndDate().toString()).isEqualTo(promotion.getEndDate().toString());
    assertThat(foundPromotion.getCreatedAt()).isEqualTo(promotion.getCreatedAt());
    assertThat(foundPromotion.getUpdatedAt()).isEqualTo(promotion.getUpdatedAt());
    assertThat(foundPromotion.getPromotionApplies().size()).isEqualTo(promotion.getPromotionApplies().size());

    verify(this.promotionGateway, times(1)).findPromotionById(promotion.getId());
  }

  @Test
  @DisplayName("getPromotionByIdFailsByPromotionNotFound - Should throw a DataNotFoundException if the promotion is not found")
  void getPromotionByIdFailsByPromotionNotFound() {
    final UUID promotionId = this.dataMock.getPromotionsDomain().getFirst().getId();

    when(this.promotionGateway.findPromotionById(promotionId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getPromotionByIdUseCase.execute(promotionId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Promoção de id: '%s' não encontrada", promotionId);

    verify(this.promotionGateway, times(1)).findPromotionById(promotionId);
  }
}
