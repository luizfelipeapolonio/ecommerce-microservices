package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.CreateProductDomainDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public record CreateProductDTO(
  @NotBlank(message = "O nome do produto é obrigatório e não deve estar em branco")
  @Length(max = 300, message = "O nome do produto pode ter no máximo 300 caracteres")
  @Schema(name = "name", type = "string", example = "Mouse wireless Logitech G PRO")
  String name,

  @NotBlank(message = "A descrição do produto é obrigatória e não deve estar em branco")
  @Schema(name = "description", type = "string", example = "A technical and descriptive text about the product")
  String description,

  @NotBlank(message = "O preço unitário do produto é obrigatório e não deve estar em branco")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 1200.00")
  @Schema(name = "unitPrice", type = "string", example = "120.00")
  String unitPrice,

  @NotNull(message = "A quantidade de unidades do produto é obrigatória")
  @PositiveOrZero(message = "A quantidade de unidades do produto não deve ser um número negativo")
  @Schema(name = "quantity", type = "integer", format = "int64", example = "50")
  Long quantity,

  @NotNull(message = "O id da categoria é obrigatório")
  @PositiveOrZero(message = "O id da categoria não deve ser um número negativo")
  @Schema(name = "categoryId", type = "integer", format = "int64", example = "1")
  Long categoryId,

  @NotNull(message = "O id da marca é obrigatório")
  @PositiveOrZero(message = "O id da marca não deve ser um número negativo")
  @Schema(name = "brandId", type = "integer", format = "int64", example = "1")
  Long brandId,

  @NotNull(message = "O id do modelo é obrigatório")
  @PositiveOrZero(message = "O id do modelo não deve ser um número negativo")
  @Schema(name = "modelId", type = "integer", format = "int64", example = "1")
  Long modelId
) implements CreateProductDomainDTO {}
