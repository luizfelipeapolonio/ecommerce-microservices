package com.felipe.ecommerce_inventory_service.infrastructure.dtos.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public record CreateSubcategoryDTO(
  @NotBlank(message = "O nome da subcategoria é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O nome da subcategoria deve ter até 100 caracteres")
  @Schema(name = "subcategoryName", type = "string", example = "motherboards")
  String subcategoryName,

  @NotNull(message = "O id da categoria pai é obrigatório")
  @PositiveOrZero(message = "O id da categoria pai não pode ser menor do que zero")
  @Schema(name = "parentCategoryId", type = "integer", format = "int64", example = "1")
  Long parentCategoryId
) {
  public CreateSubcategoryDTO {
    subcategoryName = subcategoryName.toLowerCase();
  }
}
