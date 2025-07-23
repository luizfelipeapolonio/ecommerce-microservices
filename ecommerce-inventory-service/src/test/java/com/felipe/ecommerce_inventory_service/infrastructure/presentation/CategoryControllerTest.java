package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.usecases.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateCategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateSubcategoryDTO;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles(value = "test")
@AutoConfigureMockMvc(addFilters = false)
public class CategoryControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockitoBean
  CreateCategoryUseCase createCategoryUseCase;

  @MockitoBean
  CreateSubcategoryUseCase createSubcategoryUseCase;

  private DataMock dataMock;
  private static final String BASE_URL = "/api/v1/categories";

  @BeforeEach
  void setUp() {
    this.dataMock = new DataMock();
  }

  @Test
  @DisplayName("createCategorySuccess - Should return a success ResponsePayload<CategoryDTO> response with the created category")
  void createCategorySuccess() throws Exception {
    Category category = this.dataMock.getCategoriesDomain().getFirst();
    CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO("Hardware");
    String jsonRequestBody = this.objectMapper.writeValueAsString(createCategoryDTO);

    when(this.createCategoryUseCase.execute(createCategoryDTO.name())).thenReturn(category);

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isCreated(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value("Categoria '" + createCategoryDTO.name() + "' criada com sucesso"),
        jsonPath("$.payload.id").value(category.getId()),
        jsonPath("$.payload.name").value(category.getName()),
        jsonPath("$.payload.createdAt").value(category.getCreatedAt().toString()),
        jsonPath("$.payload.updatedAt").value(category.getUpdatedAt().toString()),
        jsonPath("$.payload.parentCategory").isEmpty()
      );

    verify(this.createCategoryUseCase, times(1)).execute(createCategoryDTO.name());
  }

  @Test
  @DisplayName("createCategoryFailsByCategoryAlreadyExistsException - Should return an error ResponsePayload<Void> response")
  void createCategoryFailsByCategoryAlreadyExistsException() throws Exception {
    CreateCategoryDTO createCategoryDTO = new CreateCategoryDTO("Hardware");
    String jsonRequestBody = this.objectMapper.writeValueAsString(createCategoryDTO);

    when(this.createCategoryUseCase.execute(createCategoryDTO.name()))
      .thenThrow(new CategoryAlreadyExistsException(createCategoryDTO.name()));

    this.mockMvc.perform(post(BASE_URL)
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isConflict(),
        jsonPath("$.type").value(ResponseType.ERROR.getText()),
        jsonPath("$.code").value(HttpStatus.CONFLICT.value()),
        jsonPath("$.message").value("A categoria '" + createCategoryDTO.name() + "' já existe"),
        jsonPath("$.payload").isEmpty()
      );

    verify(this.createCategoryUseCase, times(1)).execute(createCategoryDTO.name());
  }

  @Test
  @DisplayName("createSubcategorySuccess - Should return a success ResponsePayload with the created subcategory")
  void createSubcategorySuccess() throws Exception {
    Category createdSubcategory = this.dataMock.getCategoriesDomain().get(1);
    CreateSubcategoryDTO subcategoryDTO = new CreateSubcategoryDTO("Motherboards", 1L);
    String jsonRequestBody = this.objectMapper.writeValueAsString(subcategoryDTO);
    CategoryDTO createdSubcategoryDTO = new CategoryDTO(createdSubcategory);

    when(this.createSubcategoryUseCase.execute(subcategoryDTO.parentCategoryId(), subcategoryDTO.subcategoryName()))
      .thenReturn(createdSubcategory);

    this.mockMvc.perform(post(BASE_URL + "/subcategory")
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isCreated(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.CREATED.value()),
        jsonPath("$.message").value("Subcategoria '" + subcategoryDTO.subcategoryName() + "' criada com sucesso"),
        jsonPath("$.payload.id").value(createdSubcategoryDTO.id()),
        jsonPath("$.payload.name").value(createdSubcategoryDTO.name()),
        jsonPath("$.payload.createdAt").value(createdSubcategoryDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(createdSubcategoryDTO.updatedAt()),
        jsonPath("$.payload.parentCategory.id").value(createdSubcategoryDTO.parentCategory().id()),
        jsonPath("$.payload.parentCategory.name").value(createdSubcategoryDTO.parentCategory().name()),
        jsonPath("$.payload.parentCategory.createdAt").value(createdSubcategoryDTO.parentCategory().createdAt()),
        jsonPath("$.payload.parentCategory.updatedAt").value(createdSubcategoryDTO.parentCategory().updatedAt()),
        jsonPath("$.payload.parentCategory.parentCategory").isEmpty()
      );

    verify(this.createSubcategoryUseCase, times(1))
      .execute(subcategoryDTO.parentCategoryId(), subcategoryDTO.subcategoryName());
  }

  @Test
  @DisplayName("createSubcategoryFailsByParentCategoryNotFound - Should return an error response with a NotFound status code")
  void createSubcategoryFailsByParentCategoryNotFound() throws Exception {
    CreateSubcategoryDTO subcategoryDTO = new CreateSubcategoryDTO("Motherboards", 1L);
    String jsonRequestBody = this.objectMapper.writeValueAsString(subcategoryDTO);

    when(this.createSubcategoryUseCase.execute(subcategoryDTO.parentCategoryId(), subcategoryDTO.subcategoryName()))
      .thenThrow(new DataNotFoundException("Categoria de id '" + subcategoryDTO.parentCategoryId() + "' não encontrada"));

    this.mockMvc.perform(post(BASE_URL + "/subcategory")
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isNotFound(),
        jsonPath("$.type").value(ResponseType.ERROR.getText()),
        jsonPath("$.code").value(HttpStatus.NOT_FOUND.value()),
        jsonPath("$.message").value("Categoria de id '" + subcategoryDTO.parentCategoryId() + "' não encontrada"),
        jsonPath("$.payload").isEmpty()
      );

    verify(this.createSubcategoryUseCase, times(1))
      .execute(subcategoryDTO.parentCategoryId(), subcategoryDTO.subcategoryName());
  }
}