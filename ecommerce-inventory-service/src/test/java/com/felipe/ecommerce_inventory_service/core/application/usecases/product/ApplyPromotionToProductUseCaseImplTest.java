package com.felipe.ecommerce_inventory_service.core.application.usecases.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
import com.felipe.ecommerce_inventory_service.core.application.gateway.ProductGateway;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.impl.ApplyPromotionToProductsUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ApplyPromotionToProductUseCaseImplTest {

  @Mock
  private ProductGateway productGateway;

  private ApplyPromotionToProductsUseCaseImpl applyPromotionUseCase;

  @BeforeEach
  void setUp() {
    this.applyPromotionUseCase = new ApplyPromotionToProductsUseCaseImpl(this.productGateway);
  }

  @Test
  @DisplayName("applyPromotionSuccess - Should successfully return the quantity of applied promotion")
  void applyPromotionSuccess() {
    final PromotionDTO promotionDTO = new PromotionDTO(
      "all",
      "fixed_amount",
      "10.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("20.00"),
      List.of()
    );

    when(this.productGateway.applyPromotionToProducts(promotionDTO)).thenReturn(4);

    int quantityOfAppliedPromotion = this.applyPromotionUseCase.execute(promotionDTO);

    assertThat(quantityOfAppliedPromotion).isEqualTo(4);
    verify(this.productGateway, times(1)).applyPromotionToProducts(promotionDTO);
  }
}
