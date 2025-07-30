package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.dtos.CategoriesDTO;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.DeleteCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetAllCategoriesUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.GetCategoryByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.category.UpdateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.CategoryApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateOrUpdateCategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateSubcategoryDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController implements CategoryApi {
  private final CreateCategoryUseCase createCategoryUseCase;
  private final CreateSubcategoryUseCase createSubcategoryUseCase;
  private final UpdateCategoryUseCase updateCategoryUseCase;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
  private final GetAllCategoriesUseCase getAllCategoriesUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;

  public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                            CreateSubcategoryUseCase createSubcategoryUseCase,
                            UpdateCategoryUseCase updateCategoryUseCase,
                            GetCategoryByIdUseCase getCategoryByIdUseCase,
                            GetAllCategoriesUseCase getAllCategoriesUseCase,
                            DeleteCategoryUseCase deleteCategoryUseCase) {
    this.createCategoryUseCase = createCategoryUseCase;
    this.createSubcategoryUseCase = createSubcategoryUseCase;
    this.updateCategoryUseCase = updateCategoryUseCase;
    this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    this.getAllCategoriesUseCase = getAllCategoriesUseCase;
    this.deleteCategoryUseCase = deleteCategoryUseCase;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CategoryDTO> createCategory(@Valid @RequestBody CreateOrUpdateCategoryDTO categoryDTO) {
    Category createdCategory = this.createCategoryUseCase.execute(categoryDTO.name());
    return new ResponsePayload.Builder<CategoryDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Categoria '" + createdCategory.getName() + "' criada com sucesso")
      .payload(new CategoryDTO(createdCategory))
      .build();
  }

  @Override
  @PostMapping("/subcategory")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<CategoryDTO> createSubcategory(@Valid @RequestBody CreateSubcategoryDTO createSubcategoryDTO) {
    Category createdSubcategory = this.createSubcategoryUseCase.execute(createSubcategoryDTO.parentCategoryId(),
                                                                        createSubcategoryDTO.subcategoryName());
    return new ResponsePayload.Builder<CategoryDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Subcategoria '" + createdSubcategory.getName() + "' criada com sucesso")
      .payload(new CategoryDTO(createdSubcategory))
      .build();
  }

  @Override
  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CategoryDTO> updateCategory(@Positive(message = "O id da categoria não deve ser zero, nem valores negativos")
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody CreateOrUpdateCategoryDTO categoryDTO) {
    Category updatedCategory = this.updateCategoryUseCase.execute(id, categoryDTO.name());
    return new ResponsePayload.Builder<CategoryDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Categoria atualizada com sucesso")
      .payload(new CategoryDTO(updatedCategory))
      .build();
  }

  @Override
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<CategoryDTO> getCategoryById(@Positive(message = "O id da categoria não deve ser zero, nem valores negativos")
                                                      @PathVariable Long id) {
    Category category = this.getCategoryByIdUseCase.execute(id);
    return new ResponsePayload.Builder<CategoryDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Categoria de id '" + id + "' encontrada")
      .payload(new CategoryDTO(category))
      .build();
  }

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<CategoriesDTO>> getAllCategories() {
    List<CategoriesDTO> categories = this.getAllCategoriesUseCase.execute();
    return new ResponsePayload.Builder<List<CategoriesDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todas as categorias")
      .payload(categories)
      .build();
  }

  @Override
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<Void> deleteCategory(@Positive(message = "O id da categoria não deve ser zero, nem valores negativos")
                                              @PathVariable Long id) {
    Category deletedCategory = this.deleteCategoryUseCase.execute(id);
    return new ResponsePayload.Builder<Void>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Categoria '" + deletedCategory.getName() + "' excluída com sucesso")
      .payload(null)
      .build();
  }
}
