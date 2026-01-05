package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetAllPromotionsByDiscountTypeUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.enums.DiscountType;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class GetAllPromotionsByDiscountTypeUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private GetAllPromotionsByDiscountTypeUseCaseImpl getAllPromotionsByDiscountTypeUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getAllPromotionsByDiscountTypeUseCase = new GetAllPromotionsByDiscountTypeUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getAllPromotionsByDiscountTypeSuccess - Should successfully return a list with all promotions selected by discount type")
  void getAllPromotionsByDiscountTypeSuccess() {
    final List<Promotion> promotions = this.dataMock.getPromotionsDomain();
    final String discountType = "fixed_amount";

    when(this.promotionGateway.findAllPromotionsByDiscountType(DiscountType.FIXED_AMOUNT)).thenReturn(promotions);

    List<Promotion> allPromotions = this.getAllPromotionsByDiscountTypeUseCase.execute(discountType);

    assertThat(allPromotions.size()).isEqualTo(promotions.size());
    verify(this.promotionGateway, times(1)).findAllPromotionsByDiscountType(DiscountType.FIXED_AMOUNT);
  }
}
