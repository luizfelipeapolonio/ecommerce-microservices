package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.CreateModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
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

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles(value = "test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class ModelControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CreateModelUseCase createModelUseCase;

  private static final String BASE_URL = "/api/v1/models";
  private DataMock dataMock;

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createModelSuccess - Should return a success response with the created model")
  void createModelSuccess() throws Exception {
    Model model = this.dataMock.getModelsDomain().getFirst();
    CreateModelDTO createModelDTO = new CreateModelDTO("g pro", "A great model", 1L);
    String jsonRequestBody = this.objectMapper.writeValueAsString(createModelDTO);
    ModelDTO modelDTO = new ModelDTO(model);

    when(this.createModelUseCase.execute(createModelDTO.name(), createModelDTO.description(), createModelDTO.brandId())).thenReturn(model);

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isCreated(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value("Modelo '" + model.getName() + "' criado com sucesso"),
        jsonPath("$.payload.id").value(modelDTO.id()),
        jsonPath("$.payload.name").value(modelDTO.name()),
        jsonPath("$.payload.description").value(modelDTO.description()),
        jsonPath("$.payload.createdAt").value(modelDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(modelDTO.updatedAt()),
        jsonPath("$.payload.brand.id").value(modelDTO.brand().id()),
        jsonPath("$.payload.brand.name").value(modelDTO.brand().name()),
        jsonPath("$.payload.brand.description").value(modelDTO.brand().description()),
        jsonPath("$.payload.brand.createdAt").value(modelDTO.brand().createdAt()),
        jsonPath("$.payload.brand.updatedAt").value(modelDTO.brand().updatedAt())
      );

    verify(this.createModelUseCase, times(1))
      .execute(createModelDTO.name(), createModelDTO.description(), createModelDTO.brandId());
  }
}
