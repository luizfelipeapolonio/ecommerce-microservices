package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.felipe.ecommerce_inventory_service.core.application.dtos.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.CategoryAlreadyExistsException;
import com.felipe.ecommerce_inventory_service.core.application.exceptions.DataNotFoundException;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.DeleteCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetAllCategoriesUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.UpdateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateOrUpdateCategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateSubcategoryDTO;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

  @MockitoBean
  UpdateCategoryUseCase updateCategoryUseCase;

  @MockitoBean
  GetCategoryByIdUseCase getCategoryByIdUseCase;

  @MockitoBean
  GetAllCategoriesUseCase getAllCategoriesUseCase;

  @MockitoBean
  DeleteCategoryUseCase deleteCategoryUseCase;

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
    CreateOrUpdateCategoryDTO createCategoryDTO = new CreateOrUpdateCategoryDTO("Hardware");
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
    CreateOrUpdateCategoryDTO createCategoryDTO = new CreateOrUpdateCategoryDTO("Hardware");
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

  @Test
  @DisplayName("updateCategorySuccess - Should return a success response with the updated category")
  void updateCategorySuccess() throws Exception {
    CreateOrUpdateCategoryDTO createOrUpdateCategoryDTO = new CreateOrUpdateCategoryDTO("peripherals");
    String jsonRequestBody = this.objectMapper.writeValueAsString(createOrUpdateCategoryDTO);
    Category category = Category.mutate(this.dataMock.getCategoriesDomain().getFirst())
      .name(createOrUpdateCategoryDTO.name())
      .build();
    CategoryDTO categoryDTO = new CategoryDTO(category);

    when(this.updateCategoryUseCase.execute(category.getId(), createOrUpdateCategoryDTO.name())).thenReturn(category);

    this.mockMvc.perform(patch(BASE_URL + "/" + category.getId())
      .contentType(APPLICATION_JSON).content(jsonRequestBody)
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Categoria atualizada com sucesso"),
        jsonPath("$.payload.id").value(categoryDTO.id()),
        jsonPath("$.payload.name").value(categoryDTO.name()),
        jsonPath("$.payload.createdAt").value(categoryDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(categoryDTO.updatedAt()),
        jsonPath("$.payload.parentCategory").hasJsonPath()
      );

    verify(this.updateCategoryUseCase, times(1))
      .execute(category.getId(), createOrUpdateCategoryDTO.name());
  }

  @Test
  @DisplayName("getCategoryByIdSuccess - Should return a success response with the category")
  void getCategoryByIdSuccess() throws Exception {
    Category category = this.dataMock.getCategoriesDomain().getFirst();
    CategoryDTO categoryDTO = new CategoryDTO(category);

    when(this.getCategoryByIdUseCase.execute(category.getId())).thenReturn(category);

    this.mockMvc.perform(get(BASE_URL + "/" + category.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Categoria de id '" + category.getId() + "' encontrada"),
        jsonPath("$.payload.id").value(categoryDTO.id()),
        jsonPath("$.payload.name").value(categoryDTO.name()),
        jsonPath("$.payload.createdAt").value(categoryDTO.createdAt()),
        jsonPath("$.payload.updatedAt").value(categoryDTO.updatedAt()),
        jsonPath("$.payload.parentCategory").hasJsonPath()
      );

    verify(this.getCategoryByIdUseCase, times(1)).execute(category.getId());
  }

  @Test
  @DisplayName("getAllCategoriesSuccess - Should return a success response with a list of categories")
  void getAllCategoriesSuccess() throws Exception {
    List<Category> categoriesDomain = this.dataMock.getCategoriesDomain();
    List<CategoriesDTO> categories = List.of(
      new CategoriesDTO(categoriesDomain.get(0), List.of(categoriesDomain.get(1), categoriesDomain.get(2))),
      new CategoriesDTO(categoriesDomain.get(3), List.of(categoriesDomain.get(4)))
    );

    ResponsePayload<List<CategoriesDTO>> response = new ResponsePayload.Builder<List<CategoriesDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as categorias")
      .payload(categories)
      .build();

    String jsonResponseBody = this.objectMapper.writeValueAsString(response);

    when(this.getAllCategoriesUseCase.execute()).thenReturn(categories);

    this.mockMvc.perform(get(BASE_URL)
      .accept(APPLICATION_JSON))
      .andExpectAll(status().isOk(), content().json(jsonResponseBody));

    verify(this.getAllCategoriesUseCase, times(1)).execute();
  }

  @Test
  @DisplayName("deleteCategorySuccess - Should return a success response with the deleted category name")
  void deleteCategorySuccess() throws Exception {
    Category category = this.dataMock.getCategoriesDomain().getFirst();

    when(this.deleteCategoryUseCase.execute(category.getId())).thenReturn(category);

    this.mockMvc.perform(delete(BASE_URL + "/" + category.getId())
      .accept(APPLICATION_JSON))
      .andExpectAll(
        status().isOk(),
        jsonPath("$.type").value(ResponseType.SUCCESS.getText()),
        jsonPath("$.code").value(HttpStatus.OK.value()),
        jsonPath("$.message").value("Categoria '" + category.getName() + "' excluída com sucesso"),
        jsonPath("$.payload").isEmpty()
      );

    verify(this.deleteCategoryUseCase, times(1)).execute(category.getId());
  }
}