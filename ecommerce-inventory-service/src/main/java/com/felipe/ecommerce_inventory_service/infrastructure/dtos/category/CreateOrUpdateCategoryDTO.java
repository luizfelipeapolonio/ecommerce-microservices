package com.felipe.ecommerce_inventory_service.infrastructure.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateOrUpdateCategoryDTO(
  @NotBlank(message = "O nome da categoria é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O nome da categoria deve ter no máximo 100 caracteres")
  @Schema(type = "string", example = "hardware")
  String name
) {
  public CreateOrUpdateCategoryDTO {
    name = name.toLowerCase();
  }
}
