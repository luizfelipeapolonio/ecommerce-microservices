package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetAllActiveOrInactivePromotionsUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
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
public class GetAllActiveOrInactivePromotionsUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private GetAllActiveOrInactivePromotionsUseCaseImpl getAllActiveOrInactivePromotionsUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getAllActiveOrInactivePromotionsUseCase = new GetAllActiveOrInactivePromotionsUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getAllActiveOrInactivePromotionsSuccess - Should successfully return a list with all active or inactive promotions")
  void getAllActiveOrInactivePromotionsSuccess() {
    final List<Promotion> promotions = this.dataMock.getPromotionsDomain();

    when(this.promotionGateway.findAllActiveOrInactivePromotions(true)).thenReturn(promotions);

    List<Promotion> allActivePromotions = this.getAllActiveOrInactivePromotionsUseCase.execute(true);

    assertThat(allActivePromotions.size()).isEqualTo(promotions.size());
    verify(this.promotionGateway, times(1)).findAllActiveOrInactivePromotions(true);
  }
}
