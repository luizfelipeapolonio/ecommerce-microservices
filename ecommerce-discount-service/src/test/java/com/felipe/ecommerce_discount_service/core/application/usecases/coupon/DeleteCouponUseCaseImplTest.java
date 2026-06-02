package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.DeleteCouponUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCouponUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private DeleteCouponUseCase deleteCouponUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.deleteCouponUseCase = new DeleteCouponUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("deleteCouponSuccess - Should successfully delete a coupon and return the deleted coupon")
  void deleteCouponSuccess() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();

    when(this.couponGateway.findCouponById(coupon.getId())).thenReturn(Optional.of(coupon));
    when(this.couponGateway.deleteCoupon(coupon)).thenReturn(coupon);

    Coupon deletedCoupon = this.deleteCouponUseCase.execute(coupon.getId());

    assertThat(deletedCoupon.getId()).isEqualTo(coupon.getId());
    assertThat(deletedCoupon.getName()).isEqualTo(coupon.getName());
    assertThat(deletedCoupon.getCouponCode()).isEqualTo(coupon.getCouponCode());
    assertThat(deletedCoupon.getDescription()).isEqualTo(coupon.getDescription());
    assertThat(deletedCoupon.getDiscountType()).isEqualTo(coupon.getDiscountType());
    assertThat(deletedCoupon.getDiscountValue()).isEqualTo(coupon.getDiscountValue());
    assertThat(deletedCoupon.isActive()).isEqualTo(coupon.isActive());
    assertThat(deletedCoupon.getMinimumPrice()).isEqualTo(coupon.getMinimumPrice());
    assertThat(deletedCoupon.getUsageCount()).isEqualTo(coupon.getUsageCount());
    assertThat(deletedCoupon.getUsageLimit()).isEqualTo(coupon.getUsageLimit());
    assertThat(deletedCoupon.getEndDate()).isEqualTo(coupon.getEndDate());
    assertThat(deletedCoupon.getCreatedAt()).isEqualTo(coupon.getCreatedAt());
    assertThat(deletedCoupon.getUpdatedAt()).isEqualTo(coupon.getUpdatedAt());
    assertThat(deletedCoupon.getAppliedBy().size()).isEqualTo(coupon.getAppliedBy().size());

    verify(this.couponGateway, times(1)).findCouponById(coupon.getId());
    verify(this.couponGateway, times(1)).deleteCoupon(coupon);
  }

  @Test
  @DisplayName("deleteCouponFailsByCouponNotFound - Should throw a DataNotFoundException if coupon is not found")
  void deleteCouponFailsByCouponNotFound() {
    UUID couponId = UUID.randomUUID();

    when(this.couponGateway.findCouponById(couponId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.deleteCouponUseCase.execute(couponId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cupom de id '%s' não encontrado", couponId);

    verify(this.couponGateway, times(1)).findCouponById(couponId);
    verify(this.couponGateway, never()).deleteCoupon(any(Coupon.class));
  }
}