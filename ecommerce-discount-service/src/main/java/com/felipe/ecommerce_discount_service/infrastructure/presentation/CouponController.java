package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CheckIfCouponIsValidUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CreateCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.DeleteCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetAllActiveCouponsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetAllCouponsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetCouponByIdUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CouponResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CreateCouponDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.InvalidRequestParamException;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/coupons")
public class CouponController {
  private final CreateCouponUseCase createCouponUseCase;
  private final DeleteCouponUseCase deleteCouponUseCase;
  private final GetAllActiveCouponsUseCase getAllActiveCouponsUseCase;
  private final GetAllCouponsUseCase getAllCouponsUseCase;
  private final GetCouponByIdUseCase getCouponByIdUseCase;
  private final CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase;

  public CouponController(CreateCouponUseCase createCouponUseCase,
                          DeleteCouponUseCase deleteCouponUseCase,
                          GetAllActiveCouponsUseCase getAllActiveCouponsUseCase, GetAllCouponsUseCase getAllCouponsUseCase, GetCouponByIdUseCase getCouponByIdUseCase,
                          CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase) {
    this.createCouponUseCase = createCouponUseCase;
    this.deleteCouponUseCase = deleteCouponUseCase;
    this.getAllActiveCouponsUseCase = getAllActiveCouponsUseCase;
    this.getAllCouponsUseCase = getAllCouponsUseCase;
    this.getCouponByIdUseCase = getCouponByIdUseCase;
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

  @GetMapping("/active")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<CouponResponseDTO>> getAllActiveCoupons() {
    List<CouponResponseDTO> coupons = this.getAllActiveCouponsUseCase.execute()
      .stream()
      .map(CouponResponseDTO::new)
      .toList();
    return new ResponsePayload.Builder<List<CouponResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os cupons ativos")
      .payload(coupons)
      .build();
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<CouponResponseDTO>> getAllCoupons() {
    List<CouponResponseDTO> coupons = this.getAllCouponsUseCase.execute()
      .stream()
      .map(CouponResponseDTO::new)
      .toList();
    return new ResponsePayload.Builder<List<CouponResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os cupons")
      .payload(coupons)
      .build();
  }

  @GetMapping("/{couponId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CouponResponseDTO> getCouponById(@PathVariable UUID couponId) {
    Coupon coupon = this.getCouponByIdUseCase.execute(couponId);
    return new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Cupom de id '" + couponId + "' encontrado")
      .payload(new CouponResponseDTO(coupon))
      .build();
  }

  @DeleteMapping("/{couponId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CouponResponseDTO> deleteCoupon(@PathVariable UUID couponId) {
    Coupon deletedCoupon = this.deleteCouponUseCase.execute(couponId);
    return new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Cupom de id '" + couponId + "' excluído com sucesso")
      .payload(new CouponResponseDTO(deletedCoupon))
      .build();
  }
}
