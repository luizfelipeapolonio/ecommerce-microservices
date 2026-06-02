package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidCouponException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.CheckIfCouponIsValidUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckIfCouponIsValidUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.checkIfCouponIsValidUseCase = new CheckIfCouponIsValidUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("checkIfCouponIsValidSuccess - Should check if the coupon is valid by coupon code and return it")
  void checkIfCouponIsValidSuccess() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    Coupon validCoupon = this.checkIfCouponIsValidUseCase.execute(coupon.getCouponCode());

    assertThat(validCoupon.getId()).isEqualTo(coupon.getId());
    assertThat(validCoupon.getName()).isEqualTo(coupon.getName());
    assertThat(validCoupon.getCouponCode()).isEqualTo(coupon.getCouponCode());
    assertThat(validCoupon.getDescription()).isEqualTo(coupon.getDescription());
    assertThat(validCoupon.getDiscountType()).isEqualTo(coupon.getDiscountType());
    assertThat(validCoupon.getDiscountValue()).isEqualTo(coupon.getDiscountValue());
    assertThat(validCoupon.isActive()).isEqualTo(coupon.isActive());
    assertThat(validCoupon.getMinimumPrice()).isEqualTo(coupon.getMinimumPrice());
    assertThat(validCoupon.getUsageCount()).isEqualTo(coupon.getUsageCount());
    assertThat(validCoupon.getUsageLimit()).isEqualTo(coupon.getUsageLimit());
    assertThat(validCoupon.getEndDate()).isEqualTo(coupon.getEndDate());
    assertThat(validCoupon.getCreatedAt()).isEqualTo(coupon.getCreatedAt());
    assertThat(validCoupon.getUpdatedAt()).isEqualTo(coupon.getUpdatedAt());
    assertThat(validCoupon.getAppliedBy().size()).isEqualTo(coupon.getAppliedBy().size());

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
  }

  @Test
  @DisplayName("checkIfCouponIsValidFailsByCouponNotFound - Should throw a DataNotFoundException if coupon is not found")
  void checkIfCouponIsValidFailsByCouponNotFound() {
    when(this.couponGateway.findCouponByCode(anyString())).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.checkIfCouponIsValidUseCase.execute("Anything"));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cupom de código 'Anything' não encontrado");

    verify(this.couponGateway, times(1)).findCouponByCode(anyString());
  }

  @Test
  @DisplayName("checkIfCouponIsValidFailsByUsageLimitReached - Should throw an InvalidCouponException if the coupon usage limit has been reached")
  void checkIfCouponIsValidFailsByUsageLimitReached() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    coupon.usageCount(10);

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    Exception thrown = catchException(() -> this.checkIfCouponIsValidUseCase.execute(coupon.getCouponCode()));

    assertThat(thrown)
      .isExactlyInstanceOf(InvalidCouponException.class)
      .hasMessage("O cupom '%s' é inválido", coupon.getCouponCode());

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
  }
}