package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.DeletePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllActiveOrInactivePromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsByDiscountTypeUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetPromotionByIdUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.UpdatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.infrastructure.config.openapi.PromotionApi;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.CreatePromotionDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.UpdatePromotionDTOImpl;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController implements PromotionApi {
  private final CreatePromotionUseCase createPromotionUseCase;
  private final DeletePromotionUseCase deletePromotionUseCase;
  private final UpdatePromotionUseCase updatePromotionUseCase;
  private final GetPromotionByIdUseCase getPromotionByIdUseCase;
  private final GetAllPromotionsUseCase getAllPromotionsUseCase;
  private final GetAllActiveOrInactivePromotionsUseCase getAllActiveOrInactivePromotionsUseCase;
  private final GetAllPromotionsByDiscountTypeUseCase getAllPromotionsByDiscountTypeUseCase;

  public PromotionController(CreatePromotionUseCase createPromotionUseCase,
                             DeletePromotionUseCase deletePromotionUseCase,
                             UpdatePromotionUseCase updatePromotionUseCase,
                             GetPromotionByIdUseCase getPromotionByIdUseCase,
                             GetAllPromotionsUseCase getAllPromotionsUseCase,
                             GetAllActiveOrInactivePromotionsUseCase getAllActiveOrInactivePromotionsUseCase,
                             GetAllPromotionsByDiscountTypeUseCase getAllPromotionsByDiscountTypeUseCase) {
    this.createPromotionUseCase = createPromotionUseCase;
    this.deletePromotionUseCase = deletePromotionUseCase;
    this.updatePromotionUseCase = updatePromotionUseCase;
    this.getPromotionByIdUseCase = getPromotionByIdUseCase;
    this.getAllPromotionsUseCase = getAllPromotionsUseCase;
    this.getAllActiveOrInactivePromotionsUseCase = getAllActiveOrInactivePromotionsUseCase;
    this.getAllPromotionsByDiscountTypeUseCase = getAllPromotionsByDiscountTypeUseCase;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<PromotionResponseDTO> createPromotion(@Valid @RequestBody CreatePromotionDTOImpl promotionDTO) {
    final Optional<Promotion> promotion = this.createPromotionUseCase.execute(promotionDTO);
    final String message = promotion.isPresent() ?
                           "Promoção aplicada com sucesso" :
                           "A promoção não foi aplicada, pois nenhum produto atende às condições da promoção";
    final PromotionResponseDTO payload = promotion.map(PromotionResponseDTO::new).orElse(null);

    return new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message(message)
      .payload(payload)
      .build();
  }

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<PromotionResponseDTO>> getAllPromotions() {
    final List<PromotionResponseDTO> promotions = this.getAllPromotionsUseCase.execute()
      .stream()
      .map(PromotionResponseDTO::new)
      .toList();

    return new ResponsePayload.Builder<List<PromotionResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as promoções")
      .payload(promotions)
      .build();
  }

  @Override
  @DeleteMapping("/{promotionId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<Void> deletePromotion(@PathVariable UUID promotionId) {
    final Promotion deletedPromotion = this.deletePromotionUseCase.execute(promotionId);
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção '" + deletedPromotion.getName() + "' excluída com sucesso")
      .payload(null)
      .build();
  }

  @Override
  @PatchMapping("/{promotionId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PromotionResponseDTO> updatePromotion(@PathVariable UUID promotionId,
                                                               @Valid @RequestBody UpdatePromotionDTOImpl promotionDTO) {
    final Promotion updatedPromotion = this.updatePromotionUseCase.execute(promotionId, promotionDTO);
    return new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção atualizada com sucesso")
      .payload(new PromotionResponseDTO(updatedPromotion))
      .build();
  }

  @Override
  @GetMapping("/{promotionId}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<PromotionResponseDTO> getPromotionById(@PathVariable UUID promotionId) {
    final Promotion promotion = this.getPromotionByIdUseCase.execute(promotionId);
    return new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção de id: '" + promotionId + "' encontrada")
      .payload(new PromotionResponseDTO(promotion))
      .build();
  }

  @Override
  @GetMapping("/status")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<PromotionResponseDTO>> getAllActiveOrInactivePromotions(
    @RequestParam(value = "isActive", defaultValue = "true") boolean isActive
  ) {
    final List<PromotionResponseDTO> promotions = this.getAllActiveOrInactivePromotionsUseCase.execute(isActive)
      .stream()
      .map(PromotionResponseDTO::new)
      .toList();
    final String promotionsStatus = isActive ? "ativas" : "inativas";

    return new ResponsePayload.Builder<List<PromotionResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as promoções " + promotionsStatus + " encontradas")
      .payload(promotions)
      .build();
  }

  @Override
  @GetMapping("/discount")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<PromotionResponseDTO>> getAllPromotionsByDiscountType(
    @Pattern(regexp = "^(fixed_amount|percentage)$", message = "Tipo de desconto inválido! Os valores aceitos são 'fixed_amount' e 'percentage'")
    @RequestParam(value = "discountType", defaultValue = "percentage") String discountType
  ) {
    final List<PromotionResponseDTO> promotions = this.getAllPromotionsByDiscountTypeUseCase.execute(discountType)
      .stream()
      .map(PromotionResponseDTO::new)
      .toList();

    return new ResponsePayload.Builder<List<PromotionResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as promoções com desconto do tipo '" + discountType + "'")
      .payload(promotions)
      .build();
  }
}
