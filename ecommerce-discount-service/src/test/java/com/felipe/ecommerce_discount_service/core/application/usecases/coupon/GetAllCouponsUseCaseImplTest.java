package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.GetAllCouponsUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllCouponsUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private GetAllCouponsUseCase getAllCouponsUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getAllCouponsUseCase = new GetAllCouponsUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("getAllCouponsSuccess - Should successfully return a list with all found coupons")
  void getAllCouponsSuccess() {
    List<Coupon> coupons = this.dataMock.getCouponsDomain();

    when(this.couponGateway.findAllCoupons()).thenReturn(coupons);

    List<Coupon> allCoupons = this.getAllCouponsUseCase.execute();

    assertThat(allCoupons.size()).isEqualTo(coupons.size());
    verify(this.couponGateway, times(1)).findAllCoupons();
  }
}