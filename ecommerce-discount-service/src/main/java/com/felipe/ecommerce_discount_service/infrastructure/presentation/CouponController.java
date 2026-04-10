package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.ApplyCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CheckIfCouponIsValidUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CreateCouponUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CouponResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CreateCouponDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.InvalidRequestParamException;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {
  private final CreateCouponUseCase createCouponUseCase;
  private final ApplyCouponUseCase applyCouponUseCase;
  private final CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase;

  public CouponController(CreateCouponUseCase createCouponUseCase, ApplyCouponUseCase applyCouponUseCase,
                          CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase) {
    this.createCouponUseCase = createCouponUseCase;
    this.applyCouponUseCase = applyCouponUseCase;
    this.checkIfCouponIsValidUseCase = checkIfCouponIsValidUseCase;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CouponResponseDTO> createCoupon(@Valid @RequestBody CreateCouponDTOImpl couponDTO) {
    Coupon coupon = this.createCouponUseCase.execute(couponDTO);
    return new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Cupom criado com sucesso")
      .payload(new CouponResponseDTO(coupon))
      .build();
  }

  @GetMapping("/check")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CouponResponseDTO> checkIfCouponIsValid(@RequestParam(name = "couponCode", required = false) String couponCode) {
    if (couponCode == null) {
      throw new InvalidRequestParamException(
        "couponCode", null, "O parâmetro da requisição 'couponCode' não deve ser nulo. Ex: ?couponCode=COUPON20"
      );
    }
    Coupon coupon = this.checkIfCouponIsValidUseCase.execute(couponCode.toUpperCase());
    return new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Cupom '" + coupon.getCouponCode() + "'")
      .payload(new CouponResponseDTO(coupon))
      .build();
  }
}
