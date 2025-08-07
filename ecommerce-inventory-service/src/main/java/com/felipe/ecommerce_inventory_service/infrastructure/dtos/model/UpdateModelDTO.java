package com.felipe.ecommerce_inventory_service.infrastructure.dtos.model;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

public record UpdateModelDTO(
  @NullOrNotBlank(message = "O nome do modelo não deve estar em branco")
  @Length(max = 100, message = "O nome do modelo pode ter no máximo 100 caracteres")
  @Schema(name = "name", type = "string", example = "g pro", nullable = true)
  String name,

  @NullOrNotBlank(message = "A descrição não deve estar em branco")
  @Length(max = 255, message = "A descrição pode ter até 255 caracteres")
  @Schema(name = "description", type = "string", example = "A great model", nullable = true)
  String description
) {
  public UpdateModelDTO {
    if(name != null) {
      name = name.toLowerCase();
    }
  }
}
