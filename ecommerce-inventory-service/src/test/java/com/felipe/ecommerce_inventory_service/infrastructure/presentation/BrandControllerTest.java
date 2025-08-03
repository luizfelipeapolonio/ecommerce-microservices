package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.CreateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetAllBrandsUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.GetBrandByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.brand.UpdateBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Brand;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.BrandDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.CreateOrUpdateBrandDTO;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
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
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
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
public class BrandControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CreateBrandUseCase createBrandUseCase;

  @MockitoBean
  GetBrandByIdUseCase getBrandByIdUseCase;

  @MockitoBean
  GetAllBrandsUseCase getAllBrandsUseCase;

  @MockitoBean
  UpdateBrandUseCase updateBrandUseCase;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/brands";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createBrandSuccess - Should return a success response with the created brand")
  void createBrandSuccess() throws Exception {
    CreateOrUpdateBrandDTO brandDTO = new CreateOrUpdateBrandDTO("logitech", null);
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    String jsonRequestBody = this.objectMapper.writeValueAsString(brandDTO);
    BrandDTO responseDTO = new BrandDTO(brand);

    when(this.createBrandUseCase.execute(brandDTO.name(), brandDTO.description())).thenReturn(brand);

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isCreated(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value("Marca '" + brand.getName() + "' criada com sucesso"),
        jsonPath("$.payload.id").value(responseDTO.id()),
        jsonPath("$.payload.name").value(responseDTO.name()),
        jsonPath("$.payload.description").value(responseDTO.description()),
        jsonPath("$.payload.createdAt").value(responseDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(responseDTO.updatedAt())
      );

    verify(this.createBrandUseCase, times(1)).execute(brandDTO.name(), brandDTO.description());
  }

  @Test
  @DisplayName("getBrandByIdSuccess - Should return a success response with the found brand")
  void getBrandByIdSuccess() throws Exception {
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    BrandDTO brandDTO = new BrandDTO(brand);

    when(this.getBrandByIdUseCase.execute(brand.getId())).thenReturn(brand);

    this.mockMvc.perform(get(BASE_URL + "/" + brand.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Marca de id '" + brand.getId() + "' encontrada"),
        jsonPath("$.payload.id").value(brandDTO.id()),
        jsonPath("$.payload.name").value(brandDTO.name()),
        jsonPath("$.payload.description").value(brandDTO.description()),
        jsonPath("$.payload.createdAt").value(brandDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(brandDTO.updatedAt())
      );

    verify(this.getBrandByIdUseCase, times(1)).execute(brand.getId());
  }

  @Test
  @DisplayName("getAllBrandsSuccess - Should return a success response with all brands")
  void getAllBrandsSuccess() throws Exception {
    List<Brand> brandsDomain = this.dataMock.getBrandsDomain();
    List<BrandDTO> brandsDTO = brandsDomain.stream().map(BrandDTO::new).toList();
    ResponsePayload<List<BrandDTO>> response = new ResponsePayload.Builder<List<BrandDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as marcas")
      .payload(brandsDTO)
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllBrandsUseCase.execute()).thenReturn(brandsDomain);

    this.mockMvc.perform(get(BASE_URL)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllBrandsUseCase, times(1)).execute();
  }

  @Test
  @DisplayName("updateBrandSuccess - Should return a success response with the updated brand")
  void updateBrandSuccess() throws Exception {
    Brand brand = this.dataMock.getBrandsDomain().getFirst();
    CreateOrUpdateBrandDTO brandRequestDTO = new CreateOrUpdateBrandDTO("updated name", "updated description");
    String jsonRequestBody = this.objectMapper.writeValueAsString(brandRequestDTO);
    BrandDTO brandDTO = new BrandDTO(brand);

    when(this.updateBrandUseCase.execute(brand.getId(), brandRequestDTO.name(), brandRequestDTO.description())).thenReturn(brand);

    this.mockMvc.perform(patch(BASE_URL + "/" + brand.getId())
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Marca atualizada com sucesso"),
        jsonPath("$.payload.id").value(brandDTO.id()),
        jsonPath("$.payload.name").value(brandDTO.name()),
        jsonPath("$.payload.description").value(brandDTO.description()),
        jsonPath("$.payload.createdAt").value(brandDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(brandDTO.updatedAt())
      );

    verify(this.updateBrandUseCase, times(1))
      .execute(brand.getId(), brandRequestDTO.name(), brandRequestDTO.description());
  }
}
