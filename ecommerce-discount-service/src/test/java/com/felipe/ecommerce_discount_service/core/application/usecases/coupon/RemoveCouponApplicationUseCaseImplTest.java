package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.RemoveCouponApplicationUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RemoveCouponApplicationUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private RemoveCouponApplicationUseCase removeCouponApplicationUseCase;
  private static final UUID ORDER_ID = UUID.fromString("e8f026f5-1d32-4027-8491-880cd7637e76");
  private static final UUID CUSTOMER_ID = UUID.fromString("81089f34-b2af-49e9-8800-ab64cab490b4");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.removeCouponApplicationUseCase = new RemoveCouponApplicationUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("removeCouponApplicationSuccess - Should successfully remove a coupon application")
  void removeCouponApplicationSuccess() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    CouponAppliedBy appliedBy = this.dataMock.getCouponAppliedByDomain().getFirst();
    appliedBy.setCoupon(coupon);
    appliedBy.setCustomerId(CUSTOMER_ID);
    appliedBy.setOrderId(ORDER_ID);

    coupon.addAppliedBy(appliedBy);
    coupon.setUsageCount(1);

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    this.removeCouponApplicationUseCase.execute(coupon.getCouponCode(), CUSTOMER_ID, ORDER_ID);

    verify(this.couponGateway, times(1)).saveCoupon(assertArg(argumentCoupon -> {
      assertThat(argumentCoupon.getUsageCount()).isEqualTo(0);
      assertThat(argumentCoupon.getAppliedBy().isEmpty()).isTrue();
    }));
    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
  }

  @Test
  @DisplayName("removeCouponFailsByCouponNotFound - Should throw a DataNotFoundException if coupon is not found")
  void removeCouponFailsByCouponNotFound() {
    when(this.couponGateway.findCouponByCode("Anything")).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.removeCouponApplicationUseCase.execute("Anything", CUSTOMER_ID, ORDER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cupom 'Anything' não encontrado");

    verify(this.couponGateway, times(1)).findCouponByCode("Anything");
    verify(this.couponGateway, never()).saveCoupon(any(Coupon.class));
  }

  @Test
  @DisplayName("removeCouponApplicationDoNothingIfAppliedByIsEmpty - Should do nothing if the coupon applied by list is empty")
  void removeCouponApplicationDoNothingIfAppliedByIsEmpty() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    this.removeCouponApplicationUseCase.execute(coupon.getCouponCode(), CUSTOMER_ID, ORDER_ID);

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
    verify(this.couponGateway, never()).saveCoupon(any(Coupon.class));
  }
}