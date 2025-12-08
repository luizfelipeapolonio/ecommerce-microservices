package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.EndDateDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.UpdatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.UpdatePromotionUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ExtendWith(MockitoExtension.class)
public class UpdatePromotionUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private UpdatePromotionUseCaseImpl updatePromotionUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.updatePromotionUseCase = new UpdatePromotionUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("updatePromotionSuccess - Should successfully update a promotion and return it")
  void updatePromotionSuccess() {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();
    final UpdatePromotionDTOImpl promotionDTO = new UpdatePromotionDTOImpl(
      "Updated promotion name",
      "Updated description",
      new EndDateDTOImpl(8, 12, 2099, 13, 0, 0)
    );
    final LocalDateTime updatedEndDate = convertEndDate(promotionDTO.endDate());
    ArgumentCaptor<Promotion> promotionCaptor = ArgumentCaptor.forClass(Promotion.class);

    when(this.promotionGateway.findActivePromotionById(promotion.getId())).thenReturn(Optional.of(promotion));
    when(this.promotionGateway.updatePromotion(promotionCaptor.capture())).thenReturn(promotion);

    final Promotion updatedPromotion = this.updatePromotionUseCase.execute(promotion.getId(), promotionDTO);

    // Captor assertions
    assertThat(promotionCaptor.getValue().getName()).isEqualTo(promotionDTO.name());
    assertThat(promotionCaptor.getValue().getDescription()).isEqualTo(promotionDTO.description());
    assertThat(promotionCaptor.getValue().getEndDate()).isEqualTo(updatedEndDate);
    // Updated promotion assertions
    assertThat(updatedPromotion.getId()).isEqualTo(promotion.getId());
    assertThat(updatedPromotion.getName()).isEqualTo(promotion.getName());
    assertThat(updatedPromotion.getDescription()).isEqualTo(promotion.getDescription());
    assertThat(updatedPromotion.isActive()).isEqualTo(promotion.isActive());
    assertThat(updatedPromotion.getScope()).isEqualTo(promotion.getScope());
    assertThat(updatedPromotion.getMinimumPrice()).isEqualTo(promotion.getMinimumPrice());
    assertThat(updatedPromotion.getDiscountType()).isEqualTo(promotion.getDiscountType());
    assertThat(updatedPromotion.getDiscountValue()).isEqualTo(promotion.getDiscountValue());
    assertThat(updatedPromotion.getEndDate()).isEqualTo(promotion.getEndDate());
    assertThat(updatedPromotion.getCreatedAt()).isEqualTo(promotion.getCreatedAt());
    assertThat(updatedPromotion.getUpdatedAt()).isEqualTo(promotion.getUpdatedAt());
    assertThat(updatedPromotion.getPromotionApplies().size()).isEqualTo(promotion.getPromotionApplies().size());

    verify(this.promotionGateway, times(1)).findActivePromotionById(promotion.getId());
    verify(this.promotionGateway, times(1)).updatePromotion(any(Promotion.class));
  }

  @Test
  @DisplayName("updatePromotionFailsByInvalidEndDate - Should throw an InvalidEndDateException if the updated end date is invalid")
  void updatePromotionFailsByInvalidEndDate() {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();
    final UpdatePromotionDTOImpl promotionDTO = new UpdatePromotionDTOImpl(
      "Updated promotion name",
      "Updated description",
      new EndDateDTOImpl(8, 12, 2024, 13, 0, 0)
    );
    final LocalDateTime updatedEndDate = convertEndDate(promotionDTO.endDate());

    when(this.promotionGateway.findActivePromotionById(promotion.getId())).thenReturn(Optional.of(promotion));

    Exception thrown = catchException(() -> this.updatePromotionUseCase.execute(promotion.getId(), promotionDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(InvalidEndDateException.class)
      .hasMessage(
        "Data de término inválida! " +
        "A data de término não deve ser antes da data atual. Data inválida: %s", updatedEndDate
      );

    verify(this.promotionGateway, times(1)).findActivePromotionById(promotion.getId());
    verify(this.promotionGateway, never()).updatePromotion(any(Promotion.class));
  }

  @Test
  @DisplayName("updatePromotionFailsByPromotionNotFound - Should throw a DataNotFoundException if the promotion is not found")
  void updatePromotionFailsByPromotionNotFound() {
    final UUID promotionId = this.dataMock.getPromotionsDomain().getFirst().getId();
    final UpdatePromotionDTOImpl promotionDTO = new UpdatePromotionDTOImpl(
      "Updated promotion name",
      "Updated description",
      new EndDateDTOImpl(8, 12, 2024, 13, 0, 0)
    );

    when(this.promotionGateway.findActivePromotionById(promotionId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.updatePromotionUseCase.execute(promotionId, promotionDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Promoção de id: '%s' não encontrada", promotionId);

    verify(this.promotionGateway, times(1)).findActivePromotionById(promotionId);
    verify(this.promotionGateway, never()).updatePromotion(any(Promotion.class));
  }

  private LocalDateTime convertEndDate(EndDateDTO endDate) {
    final LocalDate date = LocalDate.of(endDate.year(), endDate.month(), endDate.dayOfMonth());
    final LocalTime time = LocalTime.of(endDate.hour(), endDate.minute(), endDate.second());
    return LocalDateTime.of(date, time);
  }

  private record UpdatePromotionDTOImpl(
    String name,
    String description,
    EndDateDTOImpl endDate
  ) implements UpdatePromotionDTO {}

  private record EndDateDTOImpl(
    int dayOfMonth,
    int month,
    int year,
    int hour,
    int minute,
    int second
  ) implements EndDateDTO {}
}
