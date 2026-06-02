package com.felipe.ecommerce_discount_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CheckIfCouponIsValidUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.CreateCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.DeleteCouponUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetAllActiveCouponsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetAllCouponsUseCase;
import com.felipe.ecommerce_discount_service.core.application.usecases.coupon.GetCouponByIdUseCase;
import com.felipe.ecommerce_discount_service.core.domain.Coupon;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.EndDateDTOImpl;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CouponResponseDTO;
import com.felipe.ecommerce_discount_service.infrastructure.dtos.coupon.CreateCouponDTOImpl;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
@Import({OAuth2TestMockConfiguration.class})
@EnableAutoConfiguration(exclude = {KafkaAutoConfiguration.class})
class CouponControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  private CreateCouponUseCase createCouponUseCase;

  @MockitoBean
  private CheckIfCouponIsValidUseCase checkIfCouponIsValidUseCase;

  @MockitoBean
  private GetAllActiveCouponsUseCase getAllActiveCouponsUseCase;

  @MockitoBean
  private GetAllCouponsUseCase getAllCouponsUseCase;

  @MockitoBean
  private GetCouponByIdUseCase getCouponByIdUseCase;

  @MockitoBean
  private DeleteCouponUseCase deleteCouponUseCase;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/coupons";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCouponSuccess - Should return a ResponsePayload with a success response")
  void createCouponSuccess() throws Exception {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    CreateCouponDTOImpl couponDTO = new CreateCouponDTOImpl("Coupon 20% OFF",
      "Coupon description",
      "20%OFF",
      "percentage",
      "20.00",
      "100.00",
      new EndDateDTOImpl(10, 4, 2025, 13, 0, 0),
      10
    );
    var response = new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Cupom criado com sucesso")
      .payload(new CouponResponseDTO(coupon))
      .build();

    String jsonRequestBody = this.objectMapper.writeValueAsString(couponDTO);
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.createCouponUseCase.execute(couponDTO)).thenReturn(coupon);

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isCreated(), content().json(jsonResponseBody));

    verify(this.createCouponUseCase, times(1)).execute(couponDTO);
  }

  @Test
  @DisplayName("checkIfCouponIsValidSuccess - Should return a ResponsePayload with a success response")
  void checkIfCouponIsValidSuccess() throws Exception {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    var response = new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Cupom '" + coupon.getCouponCode() + "'")
      .payload(new CouponResponseDTO(coupon))
      .build();

    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.checkIfCouponIsValidUseCase.execute(coupon.getCouponCode())).thenReturn(coupon);

    this.mockMvc.perform(get(BASE_URL + "/check?couponCode={couponCode}", coupon.getCouponCode())
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.checkIfCouponIsValidUseCase, times(1)).execute(coupon.getCouponCode());
  }

  @Test
  @DisplayName("checkIfCouponIsValidFailsByNullRequestParam - Should return an error response")
  void checkIfCouponIsValidFailsByNullRequestParam() throws Exception {
    this.mockMvc.perform(get(BASE_URL + "/check?")
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isUnprocessableEntity(),
        jsonPath("$.type").value(ResponseType.ERROR.getText()),
        jsonPath("$.code").value(HttpStatus.UNPROCESSABLE_ENTITY.value()),
        jsonPath("$.message").value("Erro de validação"),
        jsonPath("$.payload.field").value("couponCode"),
        jsonPath("$.payload.cause").value("O parâmetro da requisição 'couponCode' não deve ser nulo. Ex: ?couponCode=COUPON20"),
        jsonPath("$.payload.rejectedValue").isEmpty()
      );

    verify(this.checkIfCouponIsValidUseCase, never()).execute(anyString());
  }

  @Test
  @DisplayName("getAllActiveCouponsSuccess - Should return a ResponsePayload with a success response")
  void getAllActiveCouponsSuccess() throws Exception {
    List<CouponResponseDTO> coupons = this.dataMock.getCouponsDomain()
      .stream()
      .map(CouponResponseDTO::new)
      .toList();
    var response = new ResponsePayload.Builder<List<CouponResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os cupons ativos")
      .payload(coupons)
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllActiveCouponsUseCase.execute()).thenReturn(this.dataMock.getCouponsDomain());

    this.mockMvc.perform(get(BASE_URL + "/active")
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllActiveCouponsUseCase, times(1)).execute();
  }

  @Test
  @DisplayName("getAllCouponsSuccess - Should return a ResponsePayload with a success response")
  void getAllCouponsSuccess() throws Exception {
    List<CouponResponseDTO> coupons = this.dataMock.getCouponsDomain()
      .stream()
      .map(CouponResponseDTO::new)
      .toList();
    var response = new ResponsePayload.Builder<List<CouponResponseDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os cupons")
      .payload(coupons)
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllCouponsUseCase.execute()).thenReturn(this.dataMock.getCouponsDomain());

    this.mockMvc.perform(get(BASE_URL)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllCouponsUseCase, times(1)).execute();
  }

  @Test
  @DisplayName("getCouponByIdSuccess - Should return a ResponsePayload with a success response")
  void getCouponByIdSuccess() throws Exception {
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    var response = new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Cupom de id '" + coupon.getId() + "' encontrado")
      .payload(new CouponResponseDTO(coupon))
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getCouponByIdUseCase.execute(coupon.getId())).thenReturn(coupon);

    this.mockMvc.perform(get(BASE_URL + "/{couponId}", coupon.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getCouponByIdUseCase, times(1)).execute(coupon.getId());
  }

  @Test
  @DisplayName("deleteCouponSuccess - Should return a ResponsePayload with a success response")
  void deleteCouponSuccess() throws Exception{
    Coupon coupon = this.dataMock.getCouponsDomain().getFirst();
    var response = new ResponsePayload.Builder<CouponResponseDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Cupom de id '" + coupon.getId() + "' excluído com sucesso")
      .payload(new CouponResponseDTO(coupon))
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.deleteCouponUseCase.execute(coupon.getId())).thenReturn(coupon);

    this.mockMvc.perform(delete(BASE_URL + "/{couponId}", coupon.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.deleteCouponUseCase, times(1)).execute(coupon.getId());
  }
}