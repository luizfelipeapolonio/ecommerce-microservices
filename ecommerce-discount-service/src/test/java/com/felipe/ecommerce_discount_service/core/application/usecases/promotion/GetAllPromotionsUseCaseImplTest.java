package com.felipe.ecommerce_discount_service.core.application.usecases.promotion;

import com.felipe.ecommerce_discount_service.core.application.gateway.PromotionGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.impl.GetAllPromotionsUseCaseImpl;
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
public class GetAllPromotionsUseCaseImplTest {

  @Mock
  private PromotionGateway promotionGateway;

  private GetAllPromotionsUseCaseImpl getAllPromotionsUseCase;
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.getAllPromotionsUseCase = new GetAllPromotionsUseCaseImpl(this.promotionGateway);
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("getAllPromotionsSuccess - Should successfully return a list with all promotions")
  void getAllPromotionsSuccess() {
    final List<Promotion> promotions = this.dataMock.getPromotionsDomain();

    when(this.promotionGateway.findAllPromotions()).thenReturn(promotions);

    List<Promotion> allPromotions = this.getAllPromotionsUseCase.execute();

    assertThat(allPromotions.size()).isEqualTo(promotions.size());
    verify(this.promotionGateway, times(1)).findAllPromotions();
  }
}
