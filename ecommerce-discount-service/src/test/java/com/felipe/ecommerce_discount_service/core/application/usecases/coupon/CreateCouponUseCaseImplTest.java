package com.felipe.ecommerce_discount_service.core.application.usecases.coupon;

import com.felipe.ecommerce_discount_service.core.application.dtos.EndDateDTO;
import com.felipe.ecommerce_discount_service.core.application.dtos.coupon.CreateCouponDTO;
import com.felipe.ecommerce_discount_service.core.application.exceptions.InvalidEndDateException;
import com.felipe.ecommerce_discount_service.core.application.gateway.CouponGateway;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.impl.CreateCouponUseCaseImpl;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCouponUseCaseImplTest {

  @Mock
  private CouponGateway couponGateway;
  private DataMock dataMock;
  private CreateCouponUseCase createCouponUseCase;
  private CreateCouponDTO validCoupon;
  private CreateCouponDTO invalidCoupon;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
    this.createCouponUseCase = new CreateCouponUseCaseImpl(this.couponGateway);
    this.validCoupon = new CreateCouponDTOImpl(
      "Coupon 20% OFF",
      "Coupon description",
      "20%OFF",
      "percentage",
      "20.00",
      "100.00",
      new EndDateDTOImpl(generateValidEndDate()),
      10
    );
    this.invalidCoupon = new CreateCouponDTOImpl(
      "Coupon 20% OFF",
      "Coupon description",
      "20%OFF",
      "percentage",
      "20.00",
      "100.00",
      new EndDateDTOImpl(10, 4, 2025, 13, 0, 0),
      10
    );
  }

  @Test
  @DisplayName("createCouponSuccess - Should successfully create a coupon and return it")
  void createCouponSuccess() {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();

    when(this.couponGateway.createCoupon(any(Coupon.class))).thenReturn(coupon);

    Coupon createdCoupon = this.createCouponUseCase.execute(this.validCoupon);

    assertThat(createdCoupon).usingRecursiveAssertion().isEqualTo(coupon);
    verify(this.couponGateway, times(1)).createCoupon(assertArg(argumentCoupon -> {
      assertThat(argumentCoupon.getName()).isEqualTo(this.validCoupon.name());
      assertThat(argumentCoupon.getDescription()).isEqualTo(this.validCoupon.description());
      assertThat(argumentCoupon.getCouponCode()).isEqualTo(this.validCoupon.couponCode());
      assertThat(argumentCoupon.getMinimumPrice()).isEqualTo(this.validCoupon.minimumPrice());
      assertThat(argumentCoupon.getDiscountType()).isEqualTo(this.validCoupon.discountType());
      assertThat(argumentCoupon.getDiscountValue()).isEqualTo(this.validCoupon.discountValue());
      assertThat(argumentCoupon.getUsageLimit()).isEqualTo(this.validCoupon.usageLimit());
      assertThat(argumentCoupon.getEndDate().getDayOfMonth()).isEqualTo(this.validCoupon.endDate().dayOfMonth());
      assertThat(argumentCoupon.getEndDate().getMonthValue()).isEqualTo(this.validCoupon.endDate().month());
      assertThat(argumentCoupon.getEndDate().getYear()).isEqualTo(this.validCoupon.endDate().year());
      assertThat(argumentCoupon.getEndDate().getHour()).isEqualTo(this.validCoupon.endDate().hour());
      assertThat(argumentCoupon.getEndDate().getMinute()).isEqualTo(this.validCoupon.endDate().minute());
      assertThat(argumentCoupon.getEndDate().getSecond()).isEqualTo(this.validCoupon.endDate().second());
    }));
  }

  @Test
  @DisplayName("createCouponFailsByInvalidEndDate - Should throw an InvalidEndDateException if the coupon end date is invalid")
  void createCouponFailsByInvalidEndDate() {
    Exception thrown = catchException(() -> this.createCouponUseCase.execute(this.invalidCoupon));

    assertThat(thrown)
      .isExactlyInstanceOf(InvalidEndDateException.class)
      .hasMessage(
        "Data de término inválida! " +
        "A data de término não deve ser antes da data atual. Data inválida: " + convertToLocalDateTime(this.invalidCoupon.endDate())
      );

    verify(this.couponGateway, never()).createCoupon(any(Coupon.class));
  }

  private record CreateCouponDTOImpl(String name,
                                     String description,
                                     String couponCode,
                                     String discountType,
                                     String discountValue,
                                     String minimumPrice,
                                     EndDateDTO endDate,
                                     Integer usageLimit) implements CreateCouponDTO {}

  private record EndDateDTOImpl(int dayOfMonth,
                                int month,
                                int year,
                                int hour,
                                int minute,
                                int second) implements EndDateDTO {
    EndDateDTOImpl(LocalDateTime localDateTime) {
      this(
        localDateTime.getDayOfMonth(),
        localDateTime.getMonthValue(),
        localDateTime.getYear(),
        localDateTime.getHour(),
        localDateTime.getMinute(),
        localDateTime.getSecond()
      );
    }
  }

  private LocalDateTime generateValidEndDate() {
    return LocalDateTime.now().plusDays(2);
  }

  private LocalDateTime convertToLocalDateTime(EndDateDTO endDateDTO) {
    LocalDate date = LocalDate.of(endDateDTO.year(), endDateDTO.month(), endDateDTO.dayOfMonth());
    LocalTime time = LocalTime.of(endDateDTO.hour(), endDateDTO.minute(), endDateDTO.second());
    return LocalDateTime.of(date, time);
  }
}