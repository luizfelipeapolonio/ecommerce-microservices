package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.CreatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.CreatePromotionDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.EndDateDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionResponseDTO;
import com.felipe.ecommerce_discount_service.testutils.DataMock;
import com.felipe.ecommerce_discount_service.testutils.OAuth2TestMockConfiguration;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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
  CreatePromotionUseCase createPromotionUseCase;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/promotions";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createPromotionWithPromotionPresent - Should return a success response with the created promotion")
  void createPromotionWithPromotionPresent() throws Exception {
    final List<PromotionAppliesTo> targetsDomain = this.dataMock.getPromotionAppliesToDomain();
    final Promotion promotionDomain = Promotion.mutate(this.dataMock.getPromotionsDomain().getFirst())
      .promotionApplies(List.of(targetsDomain.get(0), targetsDomain.get(1)))
      .build();

    final CreatePromotionDTO promotionDTO = new CreatePromotionDTOImpl(
      "Promotion 1",
      "Description of Promotion 1",
      "all",
      "fixed_amount",
      "10.00",
      "20.00",
      new EndDateDTOImpl(10, 2, 2099, 15, 0, 0),
      List.of(new PromotionAppliesToDTOImpl("category", "1"), new PromotionAppliesToDTOImpl("brand", "1"))
    );
    final var response = new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Promoção aplicada com sucesso")
      .payload(new PromotionResponseDTO(promotionDomain))
      .build();

    final String jsonRequestBody = this.objectMapper.writeValueAsString(promotionDTO);
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.createPromotionUseCase.execute(promotionDTO)).thenReturn(Optional.of(promotionDomain));

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isCreated(), content().json(jsonResponseBody));

    verify(this.createPromotionUseCase, times(1)).execute(promotionDTO);
  }

  @Test
  @DisplayName("createPromotionWithPromotionEmpty - Should return a success response")
  void createPromotionWithPromotionEmpty() throws Exception {
    final CreatePromotionDTO promotionDTO = new CreatePromotionDTOImpl(
      "Promotion 1",
      "Description of Promotion 1",
      "all",
      "fixed_amount",
      "10.00",
      "20.00",
      new EndDateDTOImpl(10, 2, 2099, 15, 0, 0),
      List.of(new PromotionAppliesToDTOImpl("category", "1"), new PromotionAppliesToDTOImpl("brand", "1"))
    );
    final var response = new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("A promoção não foi aplicada, pois nenhum produto atende às condições da promoção")
      .payload(null)
      .build();

    final String jsonRequestBody = this.objectMapper.writeValueAsString(promotionDTO);
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.createPromotionUseCase.execute(promotionDTO)).thenReturn(Optional.empty());

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isCreated(), content().json(jsonResponseBody));

    verify(this.createPromotionUseCase, times(1)).execute(promotionDTO);
  }
}
