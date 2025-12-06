package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.DeletePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.infrastructure.config.openapi.PromotionApi;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.CreatePromotionDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionResponseDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController implements PromotionApi {
  private final CreatePromotionUseCase createPromotionUseCase;
  private final DeletePromotionUseCase deletePromotionUseCase;

  public PromotionController(CreatePromotionUseCase createPromotionUseCase, DeletePromotionUseCase deletePromotionUseCase) {
    this.createPromotionUseCase = createPromotionUseCase;
    this.deletePromotionUseCase = deletePromotionUseCase;
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
}
