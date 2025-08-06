package com.felipe.ecommerce_inventory_service.infrastructure.presentation;

import com.felipe.ecommerce_inventory_service.core.application.usecases.model.CreateModelUseCase;
import com.felipe.ecommerce_inventory_service.core.domain.Model;
import com.felipe.ecommerce_inventory_service.infrastructure.config.openapi.ModelApi;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.CreateModelDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.model.ModelDTO;
import com.felipe.response.ResponsePayload;
import com.felipe.response.ResponseType;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/models")
public class ModelController implements ModelApi {
  private final CreateModelUseCase createModelUseCase;

  public ModelController(CreateModelUseCase createModelUseCase) {
    this.createModelUseCase = createModelUseCase;
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
}
