package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.exceptions.CouponAlreadyAppliedException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidCouponException;
import com.felipe.ecommerce_discount_service.core.application.exceptions.MinimumPriceException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.ApplyCouponUseCaseImpl;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class ApplyCouponUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private ApplyCouponUseCase applyCouponUseCase;
  private DataMock dataMock;
  private static final UUID ORDER_ID = UUID.fromString("e8f026f5-1d32-4027-8491-880cd7637e76");
  private static final UUID CUSTOMER_ID = UUID.fromString("81089f34-b2af-49e9-8800-ab64cab490b4");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.applyCouponUseCase = new ApplyCouponUseCaseImpl(this.couponGateway);
  }

  @Test
  @DisplayName("applyCouponSuccess - Should successfully apply an existing coupon")
  void applyCouponSuccess() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));
    when(this.couponGateway.saveCoupon(coupon)).thenReturn(coupon);

    Coupon appliedCoupon = this.applyCouponUseCase.execute(coupon.getCouponCode(), "120.00", ORDER_ID, CUSTOMER_ID);

    assertThat(appliedCoupon.getId()).isEqualTo(coupon.getId());
    assertThat(appliedCoupon.getName()).isEqualTo(coupon.getName());
    assertThat(appliedCoupon.getCouponCode()).isEqualTo(coupon.getCouponCode());
    assertThat(appliedCoupon.getDescription()).isEqualTo(coupon.getDescription());
    assertThat(appliedCoupon.getDiscountType()).isEqualTo(coupon.getDiscountType());
    assertThat(appliedCoupon.getDiscountValue()).isEqualTo(coupon.getDiscountValue());
    assertThat(appliedCoupon.isActive()).isEqualTo(coupon.isActive());
    assertThat(appliedCoupon.getMinimumPrice()).isEqualTo(coupon.getMinimumPrice());
    assertThat(appliedCoupon.getUsageCount()).isEqualTo(coupon.getUsageCount());
    assertThat(appliedCoupon.getUsageLimit()).isEqualTo(coupon.getUsageLimit());
    assertThat(appliedCoupon.getEndDate()).isEqualTo(coupon.getEndDate());
    assertThat(appliedCoupon.getCreatedAt()).isEqualTo(coupon.getCreatedAt());
    assertThat(appliedCoupon.getUpdatedAt()).isEqualTo(coupon.getUpdatedAt());
    assertThat(appliedCoupon.getAppliedBy().size()).isEqualTo(coupon.getAppliedBy().size());

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
    verify(this.couponGateway, times(1)).saveCoupon(coupon);
  }

  @Test
  @DisplayName("applyCouponFailsByCouponNotFound - Should throw a DataNotFoundException if the coupon is not found")
  void applyCouponFailsByCouponNotFound() {
    String couponCode = "20%OFF";

    when(this.couponGateway.findCouponByCode(couponCode)).thenReturn(Optional.empty());

    Exception thrown = catchException(() -> this.applyCouponUseCase.execute(couponCode, "120.00", ORDER_ID, CUSTOMER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(DataNotFoundException.class)
      .hasMessage("Cupom '%s' não encontrado", couponCode);

    verify(this.couponGateway, times(1)).findCouponByCode(couponCode);
    verify(this.couponGateway, never()).saveCoupon(any(Coupon.class));
  }

  @Test
  @DisplayName("applyCouponFailsByUsageLimitReached - Should throw an InvalidCouponException if the coupon usage limit has been reached")
  void applyCouponFailsByUsageLimitReached() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    coupon.setUsageCount(10);

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    Exception thrown = catchException(() -> this.applyCouponUseCase.execute(coupon.getCouponCode(), "120.00", ORDER_ID, CUSTOMER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(InvalidCouponException.class)
      .hasMessage("O cupom '%s' é inválido", coupon.getCouponCode());

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
    verify(this.couponGateway, never()).saveCoupon(any(Coupon.class));
  }

  @Test
  @DisplayName("applyCouponFailsByOrderPriceLessThanMinimumPrice - Should throw a MinimumPriceException if the order price is incompatible with coupon minimum price")
  void applyCouponFailsByOrderPriceLessThanMinimumPrice() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    String orderPrice = "80.00";

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    Exception thrown = catchException(() -> this.applyCouponUseCase.execute(coupon.getCouponCode(), orderPrice, ORDER_ID, CUSTOMER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(MinimumPriceException.class)
      .hasMessage("Cupom não aplicado! O valor mínimo da compra para aplicar o cupom é R$ %s", coupon.getMinimumPrice().toPlainString());

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
    verify(this.couponGateway, never()).saveCoupon(any(Coupon.class));
  }

  @Test
  @DisplayName("applyCouponFailsByCouponAlreadyApplied - Should throw a CouponAlreadyAppliedException")
  void applyCouponFailsByCouponAlreadyApplied() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    CouponAppliedBy appliedBy = this.dataMock.getCouponAppliedByDomain().getFirst()
      .coupon(coupon)
      .customerId(CUSTOMER_ID)
      .orderId(ORDER_ID);
    coupon.addAppliedBy(appliedBy);

    when(this.couponGateway.findCouponByCode(coupon.getCouponCode())).thenReturn(Optional.of(coupon));

    Exception thrown = catchException(() -> this.applyCouponUseCase.execute(coupon.getCouponCode(), "120.00", ORDER_ID, CUSTOMER_ID));

    assertThat(thrown)
      .isExactlyInstanceOf(CouponAlreadyAppliedException.class)
      .hasMessage("O cupom '%s' já foi aplicado", coupon.getCouponCode());

    verify(this.couponGateway, times(1)).findCouponByCode(coupon.getCouponCode());
    verify(this.couponGateway, never()).saveCoupon(any(Coupon.class));
  }
}
