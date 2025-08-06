package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetAllModelsOfBrandUseCase;
import com.felipe.ecommerce_inventory_service.core.application.usecases.model.GetModelByIdUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.ModelApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.CreateModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/v1/models")
public class ModelController implements ModelApi {
  private final CreateModelUseCase createModelUseCase;
  private final GetModelByIdUseCase getModelByIdUseCase;
  private final GetAllModelsOfBrandUseCase getAllModelsOfBrandUseCase;

  public ModelController(CreateModelUseCase createModelUseCase,
                         GetModelByIdUseCase getModelByIdUseCase,
                         GetAllModelsOfBrandUseCase getAllModelsOfBrandUseCase) {
    this.createModelUseCase = createModelUseCase;
    this.getModelByIdUseCase = getModelByIdUseCase;
    this.getAllModelsOfBrandUseCase = getAllModelsOfBrandUseCase;
  }

  @Override
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponsePayload<ModelDTO> createModel(@Valid @RequestBody CreateModelDTO modelDTO) {
    Model createdModel = this.createModelUseCase.execute(modelDTO.name(), modelDTO.description(), modelDTO.brandId());
    return new ResponsePayload.Builder<ModelDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.CREATED)
      .message("Modelo '" + createdModel.getName() + "' criado com sucesso")
      .payload(new ModelDTO(createdModel))
      .build();
  }

  @Override
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<List<ModelDTO>> getAllModelsOfBrand(@Positive(message = "O id da marca não deve ser zero, nem valores negativos")
                                                             @RequestParam("brandId") Long brandId) {
    List<Model> models = this.getAllModelsOfBrandUseCase.execute(brandId);
    List<ModelDTO> modelsDTO = models.stream().map(ModelDTO::new).toList();
    return new ResponsePayload.Builder<List<ModelDTO>>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Todos os modelos da marca de id '" + brandId + "'")
      .payload(modelsDTO)
      .build();
  }

  @Override
  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public ResponsePayload<ModelDTO> getModelById(@Positive(message = "O id do modelo não deve ser zero, nem valores negativos")
                                                @PathVariable Long id) {
    Model model = this.getModelByIdUseCase.execute(id);
    return new ResponsePayload.Builder<ModelDTO>()
      .type(ResponseType.SUCCESS)
      .code(HttpStatus.OK)
      .message("Modelo de id '" + model.getId() + "' encontrado")
      .payload(new ModelDTO(model))
      .build();
  }
}
