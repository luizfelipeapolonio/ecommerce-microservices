package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.CreatePromotionDTO;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.CreatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.DeletePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllActiveOrInactivePromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsByDiscountTypeUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetAllPromotionsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.GetPromotionByIdUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.promotion.UpdatePromotionUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Promotion;
import com.felipe.ecommerce_discount_service.core.domain.PromotionAppliesTo;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.CreatePromotionDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.EndDateDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionAppliesToDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.PromotionResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion.UpdatePromotionDTOImpl;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({OAuth2TestMockConfiguration.class})
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
public class PromotionControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CreatePromotionUseCase createPromotionUseCase;

  @MockitoBean
  DeletePromotionUseCase deletePromotionUseCase;

  @MockitoBean
  UpdatePromotionUseCase updatePromotionUseCase;

  @MockitoBean
  GetAllPromotionsUseCase getAllPromotionsUseCase;

  @MockitoBean
  GetPromotionByIdUseCase getPromotionByIdUseCase;

  @MockitoBean
  GetAllActiveOrInactivePromotionsUseCase getAllActiveOrInactivePromotionsUseCase;

  @MockitoBean
  GetAllPromotionsByDiscountTypeUseCase getAllPromotionsByDiscountTypeUseCase;

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

  @Test
  @DisplayName("getAllPromotionsSuccess - Should return a success response with all promotions")
  void getAllPromotionsSuccess() throws Exception {
    final List<Promotion> promotions = this.dataMock.getPromotionsDomain();
    final List<PromotionResponseDTO> promotionDTOs = promotions.stream().map(PromotionResponseDTO::new).toList();
    final var response = new ResponsePayload.Builder<List<PromotionResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as promoções")
      .payload(promotionDTOs)
      .build();
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllPromotionsUseCase.execute()).thenReturn(promotions);

    this.mockMvc.perform(get(BASE_URL)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllPromotionsUseCase, times(1)).execute();
  }

  @Test
  @DisplayName("deletePromotionSuccess - Should return a success response")
  void deletePromotionSuccess() throws Exception {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();

    when(this.deletePromotionUseCase.execute(promotion.getId())).thenReturn(promotion);

    this.mockMvc.perform(delete(BASE_URL + "/{promotionId}", promotion.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Promoção '" + promotion.getName() + "' excluída com sucesso"),
        jsonPath("$.payload").isEmpty()
      );

    verify(this.deletePromotionUseCase, times(1)).execute(promotion.getId());
  }

  @Test
  @DisplayName("updatePromotionSuccess - Should return a success response with the updated promotion")
  void updatePromotionSuccess() throws Exception {
    final Promotion updatedPromotion = this.dataMock.getPromotionsDomain().getFirst();
    final UpdatePromotionDTOImpl promotionDTO = new UpdatePromotionDTOImpl(
      "Updated promotion name",
      "Updated description",
      new EndDateDTOImpl(8, 12, 2024, 13, 0, 0)
    );

    final ResponsePayload<PromotionResponseDTO> response = new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção atualizada com sucesso")
      .payload(new PromotionResponseDTO(updatedPromotion))
      .build();

    final String jsonRequestBody = this.objectMapper.writeValueAsString(promotionDTO);
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.updatePromotionUseCase.execute(updatedPromotion.getId(), promotionDTO)).thenReturn(updatedPromotion);

    this.mockMvc.perform(patch(BASE_URL + "/{promotionId}", updatedPromotion.getId())
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.updatePromotionUseCase, times(1)).execute(updatedPromotion.getId(), promotionDTO);
  }

  @Test
  @DisplayName("getPromotionByIdSuccess - Should return a success response with the found promotion")
  void getPromotionByIdSuccess() throws Exception {
    final Promotion promotion = this.dataMock.getPromotionsDomain().getFirst();
    final ResponsePayload<PromotionResponseDTO> response = new ResponsePayload.Builder<PromotionResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Promoção de id: '" + promotion.getId() + "' encontrada")
      .payload(new PromotionResponseDTO(promotion))
      .build();
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getPromotionByIdUseCase.execute(promotion.getId())).thenReturn(promotion);

    this.mockMvc.perform(get(BASE_URL + "/{promotionId}", promotion.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getPromotionByIdUseCase, times(1)).execute(promotion.getId());
  }

  @Test
  @DisplayName("getAllActiveOrInactivePromotionsSuccess - Should return a success response with all active or inactive promotions")
  void getAllActiveOrInactivePromotionsSuccess() throws Exception {
    final List<Promotion> promotions = this.dataMock.getPromotionsDomain();
    final List<PromotionResponseDTO> promotionDTOs = promotions.stream().map(PromotionResponseDTO::new).toList();
    final var response = new ResponsePayload.Builder<List<PromotionResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as promoções ativas encontradas")
      .payload(promotionDTOs)
      .build();
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllActiveOrInactivePromotionsUseCase.execute(true)).thenReturn(promotions);

    this.mockMvc.perform(get(BASE_URL + "/status?isActive=true")
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllActiveOrInactivePromotionsUseCase, times(1)).execute(true);
  }

  @Test
  @DisplayName("getAllPromotionsByDiscountTypeSuccess - Should return a success response with all found promotions")
  void getAllPromotionsByDiscountTypeSuccess() throws Exception {
    final List<Promotion> promotions = this.dataMock.getPromotionsDomain();
    final List<PromotionResponseDTO> promotionDTOs = promotions.stream().map(PromotionResponseDTO::new).toList();
    final String discountType = "fixed_amount";
    final var response = new ResponsePayload.Builder<List<PromotionResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as promoções com desconto do tipo '" + discountType + "'")
      .payload(promotionDTOs)
      .build();
    final String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllPromotionsByDiscountTypeUseCase.execute(discountType)).thenReturn(promotions);

    this.mockMvc.perform(get(BASE_URL + "/discount?discountType={discount}", discountType)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllPromotionsByDiscountTypeUseCase, times(1)).execute(discountType);
  }
}
