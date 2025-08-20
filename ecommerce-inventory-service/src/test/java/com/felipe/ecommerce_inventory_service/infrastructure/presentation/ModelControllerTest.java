package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.DeleteModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetAllModelsOfBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.UpdateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.CreateModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.UpdateModelDTO;
import com.felipe.ecommerce_inventory_service.testutils.DataMock;
import com.felipe.ecommerce_inventory_service.testutils.OAuth2TestMockConfiguration;
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
public class ModelControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CreateModelUseCase createModelUseCase;

  @MockitoBean
  GetModelByIdUseCase getModelByIdUseCase;

  @MockitoBean
  GetAllModelsOfBrandUseCase getAllModelsOfBrandUseCase;

  @MockitoBean
  UpdateModelUseCase updateModelUseCase;

  @MockitoBean
  DeleteModelUseCase deleteModelUseCase;

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

  @Test
  @DisplayName("getModelByIdSuccess - Should return a success response with the found model")
  void getModelByIdSuccess() throws Exception {
    Model model = this.dataMock.getModelsDomain().getFirst();
    ModelDTO modelDTO = new ModelDTO(model);

    when(this.getModelByIdUseCase.execute(model.getId())).thenReturn(model);

    this.mockMvc.perform(get(BASE_URL + "/" + model.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Modelo de id '" + model.getId() + "' encontrado"),
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

    verify(this.getModelByIdUseCase, times(1)).execute(model.getId());
  }

  @Test
  @DisplayName("getAllModelsOfBrandSuccess - Should return a success response with a list of ModelDTO")
  void getAllModelsOfBrandSuccess() throws Exception {
    List<Model> models = List.of(this.dataMock.getModelsDomain().get(0), this.dataMock.getModelsDomain().get(2),
                                 this.dataMock.getModelsDomain().get(3));
    List<ModelDTO> modelsDTO = models.stream().map(ModelDTO::new).toList();
    final Long brandId = 1L;

    ResponsePayload<List<ModelDTO>> response = new ResponsePayload.Builder<List<ModelDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os modelos da marca de id '" + brandId + "'")
      .payload(modelsDTO)
      .build();
    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllModelsOfBrandUseCase.execute(brandId)).thenReturn(models);

    this.mockMvc.perform(get(BASE_URL + "?brandId={brandId}", brandId)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllModelsOfBrandUseCase, times(1)).execute(brandId);
  }

  @Test
  @DisplayName("updateModelSuccess - Should return a success response with the updated model")
  void updateModelSuccess() throws Exception {
    UpdateModelDTO updateModelDTO = new UpdateModelDTO("updatedName", "updated description");
    Model model = this.dataMock.getModelsDomain().getFirst();
    ModelDTO modelDTO = new ModelDTO(model);
    String jsonRequestBody = this.objectMapper.writeValueAsString(updateModelDTO);

    when(this.updateModelUseCase.execute(model.getId(), updateModelDTO.name(), updateModelDTO.description())).thenReturn(model);

    this.mockMvc.perform(patch(BASE_URL + "/{id}", model.getId())
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Modelo atualizado com sucesso"),
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

    verify(this.updateModelUseCase, times(1))
      .execute(model.getId(), updateModelDTO.name(), updateModelDTO.description());
  }

  @Test
  @DisplayName("deleteModelSuccess - Should return a success response with the deleted model name")
  void deleteModelSuccess() throws Exception {
    Model model = this.dataMock.getModelsDomain().getFirst();

    when(this.deleteModelUseCase.execute(model.getId())).thenReturn(model);

    this.mockMvc.perform(delete(BASE_URL + "/{id}", model.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Modelo '" + model.getName() + "' excluído com sucesso"),
        jsonPath("$.payload").isEmpty()
      );

    verify(this.deleteModelUseCase, times(1)).execute(model.getId());
  }
}