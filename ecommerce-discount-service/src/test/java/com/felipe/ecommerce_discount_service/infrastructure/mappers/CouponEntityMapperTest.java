package com.felipe.ecommerce_discount_service.infrastructure.mappers;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.core.domain.CouponAppliedBy;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponAppliedByEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CouponEntityMapperTest {

  @Spy
  private CouponEntityMapper couponEntityMapper;
  private DataMock dataMock;
  private static final UUID ORDER_ID = UUID.fromString("e8f026f5-1d32-4027-8491-880cd7637e76");
  private static final UUID CUSTOMER_ID = UUID.fromString("81089f34-b2af-49e9-8800-ab64cab490b4");

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("couponEntityToDomain - Should successfully convert a CouponEntity to Coupon")
  void couponEntityToDomain() {
    CouponEntity entity = this.dataMock.getCouponsEntity().getFirst();
    CouponAppliedByEntity appliedBy = this.dataMock.getCouponAppliedByEntity().getFirst()
      .orderId(ORDER_ID)
      .customerId(CUSTOMER_ID);
    entity.addAppliedBy(appliedBy);

    Coupon convertedCoupon = this.couponEntityMapper.toDomain(entity);

    assertThat(convertedCoupon.getId()).isEqualTo(entity.getId());
    assertThat(convertedCoupon.getName()).isEqualTo(entity.getName());
    assertThat(convertedCoupon.getDescription()).isEqualTo(entity.getDescription());
    assertThat(convertedCoupon.getCouponCode()).isEqualTo(entity.getCouponCode());
    assertThat(convertedCoupon.getMinimumPrice()).isEqualTo(entity.getMinimumPrice());
    assertThat(convertedCoupon.getDiscountType()).isEqualTo(entity.getDiscountType());
    assertThat(convertedCoupon.getDiscountValue()).isEqualTo(entity.getDiscountValue());
    assertThat(convertedCoupon.getUsageCount()).isEqualTo(entity.getUsageCount());
    assertThat(convertedCoupon.getUsageLimit()).isEqualTo(entity.getUsageLimit());
    assertThat(convertedCoupon.getEndDate().toString()).isEqualTo(entity.getEndDate().toString());
    assertThat(convertedCoupon.getCreatedAt().toString()).isEqualTo(entity.getCreatedAt().toString());
    assertThat(convertedCoupon.getUpdatedAt().toString()).isEqualTo(entity.getUpdatedAt().toString());
    assertThat(convertedCoupon.getAppliedBy().getFirst()).usingRecursiveComparison().isEqualTo(entity.getAppliedBy().getFirst());

    verify(this.couponEntityMapper, times(1)).toDomain(entity);
  }

  @Test
  @DisplayName("couponDomainToEntity - Should successfully convert a Coupon to CouponEntity")
  void couponDomainToEntity() {
    Coupon domain = this.dataMock.getCouponsDomain().getFirst();
    CouponAppliedBy appliedBy = this.dataMock.getCouponAppliedByDomain().getFirst()
      .orderId(ORDER_ID)
      .customerId(CUSTOMER_ID);
    domain.addAppliedBy(appliedBy);

    CouponEntity convertedCoupon = this.couponEntityMapper.toEntity(domain);

    assertThat(convertedCoupon.getId()).isEqualTo(domain.getId());
    assertThat(convertedCoupon.getName()).isEqualTo(domain.getName());
    assertThat(convertedCoupon.getDescription()).isEqualTo(domain.getDescription());
    assertThat(convertedCoupon.getCouponCode()).isEqualTo(domain.getCouponCode());
    assertThat(convertedCoupon.getMinimumPrice()).isEqualTo(domain.getMinimumPrice());
    assertThat(convertedCoupon.getDiscountType()).isEqualTo(domain.getDiscountType());
    assertThat(convertedCoupon.getDiscountValue()).isEqualTo(domain.getDiscountValue());
    assertThat(convertedCoupon.getUsageCount()).isEqualTo(domain.getUsageCount());
    assertThat(convertedCoupon.getUsageLimit()).isEqualTo(domain.getUsageLimit());
    assertThat(convertedCoupon.getEndDate().toString()).isEqualTo(domain.getEndDate().toString());
    assertThat(convertedCoupon.getCreatedAt().toString()).isEqualTo(domain.getCreatedAt().toString());
    assertThat(convertedCoupon.getUpdatedAt().toString()).isEqualTo(domain.getUpdatedAt().toString());
    assertThat(convertedCoupon.getAppliedBy().getFirst()).usingRecursiveComparison().isEqualTo(domain.getAppliedBy().getFirst());

    verify(this.couponEntityMapper, times(1)).toEntity(domain);
  }
}