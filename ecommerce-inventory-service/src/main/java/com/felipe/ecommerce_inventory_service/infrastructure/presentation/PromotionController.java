package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.ApplyPromotionToProductsUseCase;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.PromotionApi;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController implements PromotionApi {
  private final ApplyPromotionToProductsUseCase applyPromotionToProductsUseCase;

  public PromotionController(ApplyPromotionToProductsUseCase applyPromotionToProductsUseCase) {
    this.applyPromotionToProductsUseCase = applyPromotionToProductsUseCase;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<Map<String, Integer>> applyPromotion(@RequestBody PromotionDTO promotionDTO) {
    final int appliedPromotionQuantity = this.applyPromotionToProductsUseCase.execute(promotionDTO);

    final Map<String, Integer> promotionResponse = new HashMap<>(1);
    promotionResponse.put("appliedPromotionQuantity", appliedPromotionQuantity);

    return new ResponsePayload.Builder<Map<String, Integer>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção aplicada com sucesso em " + appliedPromotionQuantity + " produto(s)")
      .payload(promotionResponse)
      .build();
  }
}
