package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.GetAllActiveCouponsUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GetAllActiveCouponsUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private GetAllActiveCouponsUseCase getAllActiveCouponsUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getAllActiveCouponsUseCase = new GetAllActiveCouponsUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("getAllActiveCouponsSuccess - Should successfully return a list with found active coupons")
  void getAllActiveCouponsSuccess() {
    List<Coupon> coupons = this.dataMock.getCouponsDomain();

    when(this.couponGateway.findAllActiveCoupons()).thenReturn(coupons);

    List<Coupon> activeCoupons = this.getAllActiveCouponsUseCase.execute();

    assertThat(activeCoupons.size()).isEqualTo(coupons.size());
    verify(this.couponGateway, times(1)).findAllActiveCoupons();
  }
}