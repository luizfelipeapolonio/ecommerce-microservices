package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.dtos.product.PromotionDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.product.ApplyPromotionToProductsUseCase;
import com.felipe.ecommerce_inventory_service.testutils.OAuth2TestMockConfiguration;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({OAuth2TestMockConfiguration.class})
public class PromotionControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  ApplyPromotionToProductsUseCase applyPromotionToProductsUseCase;

  private static final String BASE_URL = "/api/v1/promotions";

  @Test
  @DisplayName("applyPromotionSuccess - Should return a success response with the quantity of applied promotion")
  void applyPromotionSuccess() throws Exception {
    final PromotionDTO promotionDTO = new PromotionDTO(
      "all",
      "percentage",
      "20.00",
      LocalDateTime.parse("2025-07-18T21:12:28.978228256"),
      new BigDecimal("30.00"),
      List.of()
    );
    final String jsonRequestBody = this.objectMapper.writeValueAsString(promotionDTO);

    final Map<String, Integer> mapResponse = new HashMap<>(1);
    mapResponse.put("appliedPromotionQuantity", 4);

    final ResponsePayload<Map<String, Integer>> response = new ResponsePayload.Builder<Map<String, Integer>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção aplicada com sucesso em " + mapResponse.get("appliedPromotionQuantity") + " produto(s)")
      .payload(mapResponse)
      .build();
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.applyPromotionToProductsUseCase.execute(promotionDTO)).thenReturn(4);

    this.mockMvc.perform(post(BASE_URL)
      .contentType(MediaType.APPLICATION_JSON).content(jsonRequestBody)
      .accept(MediaType.APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.applyPromotionToProductsUseCase, times(1)).execute(promotionDTO);
  }
}
