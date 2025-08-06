package com.felipe.ecommerce_inventory_service.infrastructure.dtos.model;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public record CreateModelDTO(
  @NotBlank(message = "O nome é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O nome do modelo pode ter no máximo 100 caracteres")
  @Schema(name = "name", type = "string", example = "g pro")
  String name,

  @Nullable
  @NullOrNotBlank(message = "A descrição não deve estar em branco")
  @Length(max = 255, message = "A descrição pode ter até 255 caracteres")
  @Schema(name = "description", type = "string", example = "A great model", nullable = true)
  String description,

  @NotNull(message = "O id da marca é obrigatório")
  @Positive(message = "O id da marca deve ser um número positivo e diferente de zero")
  @Schema(name = "brandId", type = "integer", format = "int64", example = "1")
  Long brandId
) {
  public CreateModelDTO {
    if(name != null) {
      name = name.toLowerCase();
    }
  }
}
