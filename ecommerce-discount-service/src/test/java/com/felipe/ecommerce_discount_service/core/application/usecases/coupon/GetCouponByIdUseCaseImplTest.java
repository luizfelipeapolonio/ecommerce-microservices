package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.GetCouponByIdUseCaseImpl;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCouponByIdUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private GetCouponByIdUseCase getCouponByIdUseCase;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.getCouponByIdUseCase = new GetCouponByIdUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("getCouponByIdSuccess - Should successfully find a coupon by id and return it")
  void getCouponByIdSuccess() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();

    when(this.couponGateway.findCouponById(coupon.getId())).thenReturn(Optional.of(coupon));

    Coupon foundCoupon = this.getCouponByIdUseCase.execute(coupon.getId());

    assertThat(foundCoupon.getId()).isEqualTo(coupon.getId());
    assertThat(foundCoupon.getName()).isEqualTo(coupon.getName());
    assertThat(foundCoupon.getCouponCode()).isEqualTo(coupon.getCouponCode());
    assertThat(foundCoupon.getDescription()).isEqualTo(coupon.getDescription());
    assertThat(foundCoupon.getDiscountType()).isEqualTo(coupon.getDiscountType());
    assertThat(foundCoupon.getDiscountValue()).isEqualTo(coupon.getDiscountValue());
    assertThat(foundCoupon.isActive()).isEqualTo(coupon.isActive());
    assertThat(foundCoupon.getMinimumPrice()).isEqualTo(coupon.getMinimumPrice());
    assertThat(foundCoupon.getUsageCount()).isEqualTo(coupon.getUsageCount());
    assertThat(foundCoupon.getUsageLimit()).isEqualTo(coupon.getUsageLimit());
    assertThat(foundCoupon.getEndDate()).isEqualTo(coupon.getEndDate());
    assertThat(foundCoupon.getCreatedAt()).isEqualTo(coupon.getCreatedAt());
    assertThat(foundCoupon.getUpdatedAt()).isEqualTo(coupon.getUpdatedAt());
    assertThat(foundCoupon.getAppliedBy().size()).isEqualTo(coupon.getAppliedBy().size());

    verify(this.couponGateway, times(1)).findCouponById(coupon.getId());
  }

  @Test
  @DisplayName("getCouponByIdFailsByCouponNotFound - Should throw a DataNotFoundException if coupon is not found")
  void getCouponByIdFailsByCouponNotFound() {
    UUID couponId = this.dataMock.getCouponsDomain().getFirst().getId();

    when(this.couponGateway.findCouponById(couponId)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.getCouponByIdUseCase.execute(couponId));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cupom de id '%s' não encontrado", couponId);

    verify(this.couponGateway, times(1)).findCouponById(couponId);
  }
}