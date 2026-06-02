package com.felipe.ecommerce_discount_service.infrastructure.gateway;

import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.infrastructure.mappers.CouponEntityMapper;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.entities.coupon.CouponEntity;
import com.felipe.ecommerce_discount_service.infrastructure.persistence.repositories.CouponRepository;
import com.felipe.ecommerce_discount_service.infrastructure.services.DiscountSchedulerService;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponGatewayImplTest {

  @Mock
  private CouponRepository couponRepository;

  @Mock
  private CouponEntityMapper couponEntityMapper;

  @Mock
  private DiscountSchedulerService discountSchedulerService;

  @InjectMocks
  private CouponGatewayImpl couponGateway;

  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCouponSuccess - Should success fully create a coupon, schedule to expire and return it")
  void createCouponSuccess() {
    Coupon couponDomain = this.dataMock.getCouponsDomain().getFirst();
    CouponEntity couponEntity = this.dataMock.getCouponsEntity().getFirst();

    when(this.couponEntityMapper.toEntity(couponDomain)).thenReturn(couponEntity);
    when(this.couponRepository.save(couponEntity)).thenReturn(couponEntity);
    doNothing().when(this.discountSchedulerService).scheduleCouponToExpire(couponEntity);
    when(this.couponEntityMapper.toDomain(couponEntity)).thenReturn(couponDomain);

    Coupon createdCoupon = this.couponGateway.createCoupon(couponDomain);

    assertThat(createdCoupon.getId()).isEqualTo(couponDomain.getId());
    assertThat(createdCoupon.getName()).isEqualTo(couponDomain.getName());
    assertThat(createdCoupon.getDescription()).isEqualTo(couponDomain.getDescription());
    assertThat(createdCoupon.getCouponCode()).isEqualTo(couponDomain.getCouponCode());
    assertThat(createdCoupon.getMinimumPrice()).isEqualTo(couponDomain.getMinimumPrice());
    assertThat(createdCoupon.getDiscountType()).isEqualTo(couponDomain.getDiscountType());
    assertThat(createdCoupon.getDiscountValue()).isEqualTo(couponDomain.getDiscountValue());
    assertThat(createdCoupon.getUsageCount()).isEqualTo(couponDomain.getUsageCount());
    assertThat(createdCoupon.getUsageLimit()).isEqualTo(couponDomain.getUsageLimit());
    assertThat(createdCoupon.getEndDate().toString()).isEqualTo(couponDomain.getEndDate().toString());
    assertThat(createdCoupon.getCreatedAt().toString()).isEqualTo(couponDomain.getCreatedAt().toString());
    assertThat(createdCoupon.getUpdatedAt().toString()).isEqualTo(couponDomain.getUpdatedAt().toString());
    assertThat(createdCoupon.getAppliedBy().size()).isEqualTo(couponDomain.getAppliedBy().size());

    verify(this.couponEntityMapper, times(1)).toEntity(couponDomain);
    verify(this.couponRepository, times(1)).save(couponEntity);
    verify(this.discountSchedulerService, times(1)).scheduleCouponToExpire(couponEntity);
    verify(this.couponEntityMapper, times(1)).toDomain(couponEntity);
  }

  @Test
  @DisplayName("findCouponByCodeSuccess - Should find a coupon by code and return an optional of Coupon")
  void findCouponByCodeSuccess() {
    Coupon couponDomain = this.dataMock.getCouponsDomain().getFirst();
    CouponEntity couponEntity = this.dataMock.getCouponsEntity().getFirst();

    when(this.couponRepository.findByCouponCodeAndIsActiveTrue(couponDomain.getCouponCode())).thenReturn(Optional.of(couponEntity));
    when(this.couponEntityMapper.toDomain(couponEntity)).thenReturn(couponDomain);

    Optional<Coupon> foundCoupon = this.couponGateway.findCouponByCode(couponDomain.getCouponCode());

    assertThat(foundCoupon.isPresent()).isTrue();
    assertThat(foundCoupon.get().getId()).isEqualTo(couponDomain.getId());
    assertThat(foundCoupon.get().getName()).isEqualTo(couponDomain.getName());
    assertThat(foundCoupon.get().getDescription()).isEqualTo(couponDomain.getDescription());
    assertThat(foundCoupon.get().getCouponCode()).isEqualTo(couponDomain.getCouponCode());
    assertThat(foundCoupon.get().getMinimumPrice()).isEqualTo(couponDomain.getMinimumPrice());
    assertThat(foundCoupon.get().getDiscountType()).isEqualTo(couponDomain.getDiscountType());
    assertThat(foundCoupon.get().getDiscountValue()).isEqualTo(couponDomain.getDiscountValue());
    assertThat(foundCoupon.get().getUsageCount()).isEqualTo(couponDomain.getUsageCount());
    assertThat(foundCoupon.get().getUsageLimit()).isEqualTo(couponDomain.getUsageLimit());
    assertThat(foundCoupon.get().getEndDate().toString()).isEqualTo(couponDomain.getEndDate().toString());
    assertThat(foundCoupon.get().getCreatedAt().toString()).isEqualTo(couponDomain.getCreatedAt().toString());
    assertThat(foundCoupon.get().getUpdatedAt().toString()).isEqualTo(couponDomain.getUpdatedAt().toString());
    assertThat(foundCoupon.get().getAppliedBy().size()).isEqualTo(couponDomain.getAppliedBy().size());

    verify(this.couponRepository, times(1)).findByCouponCodeAndIsActiveTrue(couponDomain.getCouponCode());
    verify(this.couponEntityMapper, times(1)).toDomain(couponEntity);
  }

  @Test
  @DisplayName("findCouponByIdSuccess - Should find a coupon by id and return an Optional of Coupon")
  void findCouponByIdSuccess() {
    Coupon couponDomain = this.dataMock.getCouponsDomain().getFirst();
    CouponEntity couponEntity = this.dataMock.getCouponsEntity().getFirst();

    when(this.couponRepository.findById(couponDomain.getId())).thenReturn(Optional.of(couponEntity));
    when(this.couponEntityMapper.toDomain(couponEntity)).thenReturn(couponDomain);

    Optional<Coupon> foundCoupon = this.couponGateway.findCouponById(couponDomain.getId());

    assertThat(foundCoupon.isPresent()).isTrue();
    assertThat(foundCoupon.get().getId()).isEqualTo(couponDomain.getId());
    assertThat(foundCoupon.get().getName()).isEqualTo(couponDomain.getName());
    assertThat(foundCoupon.get().getDescription()).isEqualTo(couponDomain.getDescription());
    assertThat(foundCoupon.get().getCouponCode()).isEqualTo(couponDomain.getCouponCode());
    assertThat(foundCoupon.get().getMinimumPrice()).isEqualTo(couponDomain.getMinimumPrice());
    assertThat(foundCoupon.get().getDiscountType()).isEqualTo(couponDomain.getDiscountType());
    assertThat(foundCoupon.get().getDiscountValue()).isEqualTo(couponDomain.getDiscountValue());
    assertThat(foundCoupon.get().getUsageCount()).isEqualTo(couponDomain.getUsageCount());
    assertThat(foundCoupon.get().getUsageLimit()).isEqualTo(couponDomain.getUsageLimit());
    assertThat(foundCoupon.get().getEndDate().toString()).isEqualTo(couponDomain.getEndDate().toString());
    assertThat(foundCoupon.get().getCreatedAt().toString()).isEqualTo(couponDomain.getCreatedAt().toString());
    assertThat(foundCoupon.get().getUpdatedAt().toString()).isEqualTo(couponDomain.getUpdatedAt().toString());
    assertThat(foundCoupon.get().getAppliedBy().size()).isEqualTo(couponDomain.getAppliedBy().size());

    verify(this.couponRepository, times(1)).findById(couponDomain.getId());
    verify(this.couponEntityMapper, times(1)).toDomain(couponEntity);
  }

  @Test
  @DisplayName("findAllActiveCouponsSuccess - Should find all active coupons and return it")
  void findAllActiveCouponsSuccess() {
    List<Coupon> couponsDomain = this.dataMock.getCouponsDomain();
    List<CouponEntity> couponsEntity = this.dataMock.getCouponsEntity();

    when(this.couponRepository.findAllByIsActiveTrue()).thenReturn(couponsEntity);
    when(this.couponEntityMapper.toDomain(couponsEntity.get(0))).thenReturn(couponsDomain.get(0));
    when(this.couponEntityMapper.toDomain(couponsEntity.get(1))).thenReturn(couponsDomain.get(1));

    List<Coupon> allCoupons = this.couponGateway.findAllActiveCoupons();

    assertThat(allCoupons.isEmpty()).isFalse();
    assertThat(allCoupons.size()).isEqualTo(2);
    assertThat(allCoupons.get(0)).usingRecursiveAssertion().isEqualTo(couponsDomain.get(0));
    assertThat(allCoupons.get(1)).usingRecursiveAssertion().isEqualTo(couponsDomain.get(1));

    verify(this.couponRepository, times(1)).findAllByIsActiveTrue();
    verify(this.couponEntityMapper, times(2)).toDomain(any(CouponEntity.class));
    verify(this.couponEntityMapper, times(1)).toDomain(couponsEntity.get(0));
    verify(this.couponEntityMapper, times(1)).toDomain(couponsEntity.get(1));
  }

  @Test
  @DisplayName("findAllCouponsSuccess - Should find all coupons and return it")
  void findAllCouponsSuccess() {
    List<Coupon> couponsDomain = this.dataMock.getCouponsDomain();
    List<CouponEntity> couponsEntity = this.dataMock.getCouponsEntity();

    when(this.couponRepository.findAll()).thenReturn(couponsEntity);
    when(this.couponEntityMapper.toDomain(couponsEntity.get(0))).thenReturn(couponsDomain.get(0));
    when(this.couponEntityMapper.toDomain(couponsEntity.get(1))).thenReturn(couponsDomain.get(1));

    List<Coupon> allCoupons = this.couponGateway.findAllCoupons();

    assertThat(allCoupons.isEmpty()).isFalse();
    assertThat(allCoupons.size()).isEqualTo(2);
    assertThat(allCoupons.get(0)).usingRecursiveAssertion().isEqualTo(couponsDomain.get(0));
    assertThat(allCoupons.get(1)).usingRecursiveAssertion().isEqualTo(couponsDomain.get(1));

    verify(this.couponRepository, times(1)).findAll();
    verify(this.couponEntityMapper, times(2)).toDomain(any(CouponEntity.class));
    verify(this.couponEntityMapper, times(1)).toDomain(couponsEntity.get(0));
    verify(this.couponEntityMapper, times(1)).toDomain(couponsEntity.get(1));
  }

  @Test
  @DisplayName("saveCouponSuccess - Should save a coupon and return it")
  void saveCouponSuccess() {
    Coupon couponDomain = this.dataMock.getCouponsDomain().getFirst();
    CouponEntity couponEntity = this.dataMock.getCouponsEntity().getFirst();

    when(this.couponEntityMapper.toEntity(couponDomain)).thenReturn(couponEntity);
    when(this.couponRepository.save(couponEntity)).thenReturn(couponEntity);
    when(this.couponEntityMapper.toDomain(couponEntity)).thenReturn(couponDomain);

    Coupon savedCoupon = this.couponGateway.saveCoupon(couponDomain);

    assertThat(savedCoupon.getId()).isEqualTo(couponDomain.getId());
    assertThat(savedCoupon.getName()).isEqualTo(couponDomain.getName());
    assertThat(savedCoupon.getDescription()).isEqualTo(couponDomain.getDescription());
    assertThat(savedCoupon.getCouponCode()).isEqualTo(couponDomain.getCouponCode());
    assertThat(savedCoupon.getMinimumPrice()).isEqualTo(couponDomain.getMinimumPrice());
    assertThat(savedCoupon.getDiscountType()).isEqualTo(couponDomain.getDiscountType());
    assertThat(savedCoupon.getDiscountValue()).isEqualTo(couponDomain.getDiscountValue());
    assertThat(savedCoupon.getUsageCount()).isEqualTo(couponDomain.getUsageCount());
    assertThat(savedCoupon.getUsageLimit()).isEqualTo(couponDomain.getUsageLimit());
    assertThat(savedCoupon.getEndDate().toString()).isEqualTo(couponDomain.getEndDate().toString());
    assertThat(savedCoupon.getCreatedAt().toString()).isEqualTo(couponDomain.getCreatedAt().toString());
    assertThat(savedCoupon.getUpdatedAt().toString()).isEqualTo(couponDomain.getUpdatedAt().toString());
    assertThat(savedCoupon.getAppliedBy().size()).isEqualTo(couponDomain.getAppliedBy().size());

    verify(this.couponEntityMapper, times(1)).toEntity(couponDomain);
    verify(this.couponRepository, times(1)).save(couponEntity);
    verify(this.couponEntityMapper, times(1)).toDomain(couponEntity);
  }

  @Test
  @DisplayName("deleteCouponSuccess - Should delete a coupon and return it")
  void deleteCouponSuccess() {
    Coupon couponDomain = this.dataMock.getCouponsDomain().getFirst();
    CouponEntity couponEntity = this.dataMock.getCouponsEntity().getFirst();

    when(this.couponEntityMapper.toEntity(couponDomain)).thenReturn(couponEntity);
    doNothing().when(this.couponRepository).delete(couponEntity);

    Coupon deletedCoupon = this.couponGateway.deleteCoupon(couponDomain);

    assertThat(deletedCoupon.getId()).isEqualTo(couponDomain.getId());
    assertThat(deletedCoupon.getName()).isEqualTo(couponDomain.getName());
    assertThat(deletedCoupon.getDescription()).isEqualTo(couponDomain.getDescription());
    assertThat(deletedCoupon.getCouponCode()).isEqualTo(couponDomain.getCouponCode());
    assertThat(deletedCoupon.getMinimumPrice()).isEqualTo(couponDomain.getMinimumPrice());
    assertThat(deletedCoupon.getDiscountType()).isEqualTo(couponDomain.getDiscountType());
    assertThat(deletedCoupon.getDiscountValue()).isEqualTo(couponDomain.getDiscountValue());
    assertThat(deletedCoupon.getUsageCount()).isEqualTo(couponDomain.getUsageCount());
    assertThat(deletedCoupon.getUsageLimit()).isEqualTo(couponDomain.getUsageLimit());
    assertThat(deletedCoupon.getEndDate().toString()).isEqualTo(couponDomain.getEndDate().toString());
    assertThat(deletedCoupon.getCreatedAt().toString()).isEqualTo(couponDomain.getCreatedAt().toString());
    assertThat(deletedCoupon.getUpdatedAt().toString()).isEqualTo(couponDomain.getUpdatedAt().toString());
    assertThat(deletedCoupon.getAppliedBy().size()).isEqualTo(couponDomain.getAppliedBy().size());

    verify(this.couponEntityMapper, times(1)).toEntity(couponDomain);
    verify(this.couponRepository, times(1)).delete(couponEntity);
  }
}