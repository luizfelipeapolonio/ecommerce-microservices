package com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand;

import com.felipe.ecommerce_inventory_service.infrastructure.dtos.brand.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;

public record CreateOrUpdateBrandDTO(
  @NotBlank(message = "O nome da marca é obrigatório e não deve estar em branco")
  @Length(max = 100, message = "O nome da marca deve ter no máximo 100 caracteres")
  @Schema(name = "name", type = "string", example = "logitech")
  String name,

  @Nullable
  @NullOrNotBlank(message = "A descrição não deve estar em branco")
  @Length(min = 1, max = 255, message = "A descrição deve ter entre 1 e 255 caracteres")
  @Schema(name = "description", type = "string", example = "A great brand", nullable = true)
  String description
) {
  public CreateOrUpdateBrandDTO {
    if(name != null) {
      name = name.toLowerCase();
    }
  }
}