package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.usecases.CreateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.CreateSubcategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.UpdateCategoryUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Category;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.CategoryApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateOrUpdateCategoryDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.category.CreateSubcategoryDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController implements CategoryApi {
  private final CreateCategoryUseCase createCategoryUseCase;
  private final CreateSubcategoryUseCase createSubcategoryUseCase;
  private final UpdateCategoryUseCase updateCategoryUseCase;

  public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                            CreateSubcategoryUseCase createSubcategoryUseCase,
                            UpdateCategoryUseCase updateCategoryUseCase) {
    this.createCategoryUseCase = createCategoryUseCase;
    this.createSubcategoryUseCase = createSubcategoryUseCase;
    this.updateCategoryUseCase = updateCategoryUseCase;
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
  public ResponsePayload<CategoryDTO> updateCategory(@PathVariable Long id,
                                                     @Valid @RequestBody CreateOrUpdateCategoryDTO categoryDTO) {
    Category updatedCategory = this.updateCategoryUseCase.execute(id, categoryDTO.name());
    return new ResponsePayload.Builder<CategoryDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Categoria atualizada com sucesso")
      .payload(new CategoryDTO(updatedCategory))
      .build();
  }
}
