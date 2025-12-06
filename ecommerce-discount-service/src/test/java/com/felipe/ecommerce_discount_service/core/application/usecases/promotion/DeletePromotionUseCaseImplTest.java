package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.DeletePromotionUseCaseImpl;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ExtendWith(MockitoExtension.class)
public class DeletePromotionUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private DeletePromotionUseCaseImpl deletePromotionUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.deletePromotionUseCase = new DeletePromotionUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("deletePromotionSuccess - Should successfully delete a promotion and return the deleted promotion")
  void deletePromotionSuccess() {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();

    when(this.promotionGateway.findPromotionById(promotion.getId())).thenReturn(Optional.of(promotion));
    when(this.promotionGateway.deletePromotion(promotion)).thenReturn(promotion);

    Promotion deletedPromotion = this.deletePromotionUseCase.execute(promotion.getId());

    assertThat(deletedPromotion.getId()).isEqualTo(promotion.getId());
    assertThat(deletedPromotion.getName()).isEqualTo(promotion.getName());
    assertThat(deletedPromotion.getDescription()).isEqualTo(promotion.getDescription());
    assertThat(deletedPromotion.isActive()).isEqualTo(promotion.isActive());
    assertThat(deletedPromotion.getScope()).isEqualTo(promotion.getScope());
    assertThat(deletedPromotion.getMinimumPrice()).isEqualTo(promotion.getMinimumPrice());
    assertThat(deletedPromotion.getDiscountType()).isEqualTo(promotion.getDiscountType());
    assertThat(deletedPromotion.getDiscountValue()).isEqualTo(promotion.getDiscountValue());
    assertThat(deletedPromotion.getEndDate()).isEqualTo(promotion.getEndDate());
    assertThat(deletedPromotion.getCreatedAt()).isEqualTo(promotion.getCreatedAt());
    assertThat(deletedPromotion.getUpdatedAt()).isEqualTo(promotion.getUpdatedAt());
    assertThat(deletedPromotion.getPromotionApplies().size()).isEqualTo(promotion.getPromotionApplies().size());

    verify(this.promotionGateway, times(1)).findPromotionById(promotion.getId());
    verify(this.promotionGateway, times(1)).deletePromotion(promotion);
  }

  @Test
  @DisplayName("deletePromotionFailsByPromotionNotFound - Should throw a DataNotFoundException if the promotion is not found")
  void deletePromotionFailsByPromotionNotFound() {
    final UUID promotionId = this.dataMock.getPromotionsDomain().getFirst().getId();

    when(this.promotionGateway.findPromotionById(promotionId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deletePromotionUseCase.execute(promotionId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Promoção de id: '%s' não encontrada", promotionId);

    verify(this.promotionGateway, times(1)).findPromotionById(promotionId);
    verify(this.promotionGateway, never()).deletePromotion(any(Promotion.class));
  }
}
