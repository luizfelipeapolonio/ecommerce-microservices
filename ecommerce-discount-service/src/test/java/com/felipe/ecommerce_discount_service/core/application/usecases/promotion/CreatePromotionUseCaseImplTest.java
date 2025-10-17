package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.CreatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.EndDateDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.PromotionAppliesToDTO;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.CreatePromotionUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;

@ExtendWith(MockitoExtension.class)
public class CreatePromotionUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private CreatePromotionUseCaseImpl createPromotionUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.createPromotionUseCase = new CreatePromotionUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createPromotionSuccess - Should successfully create a promotion")
  void createPromotionSuccess() {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();
    final CreatePromotionDTO createPromotionDTO = new CreatePromotionDTOImpl(
      promotion.getName(),
      promotion.getDescription(),
      promotion.getScope(),
      promotion.getDiscountType(),
      promotion.getDiscountValue(),
      promotion.getMinimumPrice().toString(),
      new EndDateDTOImpl(10, 2, 2099, 14, 0, 0),
      List.of(
        new PromotionAppliesToDTOImpl("category", "1"),
        new PromotionAppliesToDTOImpl("brand", "1")
      )
    );
    final ArgumentCaptor<Promotion> promotionCaptor = ArgumentCaptor.forClass(Promotion.class);

    when(this.promotionGateway.createPromotion(promotionCaptor.capture())).thenReturn(Optional.of(promotion));

    final Optional<Promotion> createdPromotion = this.createPromotionUseCase.execute(createPromotionDTO);

    // Captor assertion
    assertThat(promotionCaptor.getValue().getMinimumPrice().toString()).isEqualTo("30.00");
    assertThat(promotionCaptor.getValue().getPromotionApplies().size()).isEqualTo(createPromotionDTO.promotionApplies().size());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(0).getTarget()).isEqualTo(createPromotionDTO.promotionApplies().get(0).target());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(0).getTargetId()).isEqualTo(createPromotionDTO.promotionApplies().get(0).targetId());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(1).getTarget()).isEqualTo(createPromotionDTO.promotionApplies().get(1).target());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(1).getTargetId()).isEqualTo(createPromotionDTO.promotionApplies().get(1).targetId());

    // Created promotion assertion
    assertThat(createdPromotion.isPresent()).isTrue();
    assertThat(createdPromotion.get().getId()).isEqualTo(promotion.getId());
    assertThat(createdPromotion.get().getName()).isEqualTo(promotion.getName());
    assertThat(createdPromotion.get().getDescription()).isEqualTo(promotion.getDescription());
    assertThat(createdPromotion.get().getDiscountType()).isEqualTo(promotion.getDiscountType());
    assertThat(createdPromotion.get().getDiscountValue()).isEqualTo(promotion.getDiscountValue());
    assertThat(createdPromotion.get().getMinimumPrice().toString()).isEqualTo(promotion.getMinimumPrice().toString());
    assertThat(createdPromotion.get().getScope()).isEqualTo(promotion.getScope());
    assertThat(createdPromotion.get().isActive()).isEqualTo(promotion.isActive());
    assertThat(createdPromotion.get().getEndDate().toString()).isEqualTo(promotion.getEndDate().toString());
    assertThat(createdPromotion.get().getCreatedAt()).isEqualTo(promotion.getCreatedAt());
    assertThat(createdPromotion.get().getUpdatedAt()).isEqualTo(promotion.getUpdatedAt());
    assertThat(createdPromotion.get().getPromotionApplies().size()).isEqualTo(promotion.getPromotionApplies().size());

    verify(this.promotionGateway, times(1)).createPromotion(any(Promotion.class));
  }

  @Test
  @DisplayName("createPromotionFailsByInvalidEndDate - Should throw an InvalidEndDateException if the end date is invalid")
  void createPromotionFailsByInvalidEndDate() {
    final CreatePromotionDTO createPromotionDTO = new CreatePromotionDTOImpl(
      "",
      "",
      "",
      "",
      "",
      "",
      new EndDateDTOImpl(10, 2, 2024, 14, 0, 0),
      List.of()
    );

    final LocalDateTime endDate = LocalDateTime.of(
      LocalDate.of(createPromotionDTO.endDate().year(), createPromotionDTO.endDate().month(), createPromotionDTO.endDate().dayOfMonth()),
      LocalTime.of(createPromotionDTO.endDate().hour(), createPromotionDTO.endDate().minute(), createPromotionDTO.endDate().second())
    );

    Exception thrown = catchException(() -> this.createPromotionUseCase.execute(createPromotionDTO));

    assertThat(thrown)
      .isExactlyInstanceOf(InvalidEndDateException.class)
      .hasMessage(
        "Data de término inválida! " +
        "A data de término não deve ser antes da data atual. Data inválida: %s", endDate
      );

    verify(this.promotionGateway, never()).createPromotion(any(Promotion.class));
  }

  @Test
  @DisplayName("createPromotionSuccessWithPercentageDiscountType - Should successfully create a promotion")
  void createPromotionSuccessWithPercentageDiscountType() {
    final Promotion promotion = Promotion.mutate(this.dataMock.getPromotionsDomain().getFirst())
      .discountType(DiscountType.PERCENTAGE)
      .build();
    final CreatePromotionDTO createPromotionDTO = new CreatePromotionDTOImpl(
      promotion.getName(),
      promotion.getDescription(),
      promotion.getScope(),
      promotion.getDiscountType(),
      promotion.getDiscountValue(),
      promotion.getMinimumPrice().toString(),
      new EndDateDTOImpl(10, 2, 2099, 14, 0, 0),
      List.of(
        new PromotionAppliesToDTOImpl("category", "1"),
        new PromotionAppliesToDTOImpl("brand", "1")
      )
    );
    final ArgumentCaptor<Promotion> promotionCaptor = ArgumentCaptor.forClass(Promotion.class);

    when(this.promotionGateway.createPromotion(promotionCaptor.capture())).thenReturn(Optional.of(promotion));

    final Optional<Promotion> createdPromotion = this.createPromotionUseCase.execute(createPromotionDTO);

    // Captor assertion
    assertThat(promotionCaptor.getValue().getMinimumPrice().toString()).isEqualTo("20.00");
    assertThat(promotionCaptor.getValue().getPromotionApplies().size()).isEqualTo(createPromotionDTO.promotionApplies().size());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(0).getTarget()).isEqualTo(createPromotionDTO.promotionApplies().get(0).target());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(0).getTargetId()).isEqualTo(createPromotionDTO.promotionApplies().get(0).targetId());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(1).getTarget()).isEqualTo(createPromotionDTO.promotionApplies().get(1).target());
    assertThat(promotionCaptor.getValue().getPromotionApplies().get(1).getTargetId()).isEqualTo(createPromotionDTO.promotionApplies().get(1).targetId());

    // Created promotion assertion
    assertThat(createdPromotion.isPresent()).isTrue();
    assertThat(createdPromotion.get().getId()).isEqualTo(promotion.getId());
    assertThat(createdPromotion.get().getName()).isEqualTo(promotion.getName());
    assertThat(createdPromotion.get().getDescription()).isEqualTo(promotion.getDescription());
    assertThat(createdPromotion.get().getDiscountType()).isEqualTo(promotion.getDiscountType());
    assertThat(createdPromotion.get().getDiscountValue()).isEqualTo(promotion.getDiscountValue());
    assertThat(createdPromotion.get().getMinimumPrice().toString()).isEqualTo(promotion.getMinimumPrice().toString());
    assertThat(createdPromotion.get().getScope()).isEqualTo(promotion.getScope());
    assertThat(createdPromotion.get().isActive()).isEqualTo(promotion.isActive());
    assertThat(createdPromotion.get().getEndDate().toString()).isEqualTo(promotion.getEndDate().toString());
    assertThat(createdPromotion.get().getCreatedAt()).isEqualTo(promotion.getCreatedAt());
    assertThat(createdPromotion.get().getUpdatedAt()).isEqualTo(promotion.getUpdatedAt());
    assertThat(createdPromotion.get().getPromotionApplies().size()).isEqualTo(promotion.getPromotionApplies().size());

    verify(this.promotionGateway, times(1)).createPromotion(any(Promotion.class));
  }

  private record EndDateDTOImpl(
    int dayOfMonth,
    int month,
    int year,
    int hour,
    int minute,
    int second
  ) implements EndDateDTO {}

  private record PromotionAppliesToDTOImpl(String target, String targetId) implements PromotionAppliesToDTO {}

  private record CreatePromotionDTOImpl(
    String name,
    String description,
    String scope,
    String discountType,
    String discountValue,
    String minimumPrice,
    EndDateDTO endDate,
    List<PromotionAppliesToDTOImpl> promotionApplies
  ) implements CreatePromotionDTO {}
}
