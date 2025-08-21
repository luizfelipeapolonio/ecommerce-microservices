package com.felipe.ecommerce_inventory_service.infrastructure.dtos.product;

import com.felipe.ecommerce_inventory_service.core.application.dtos.product.UpdateProductDomainDTO;
import com.felipe.ecommerce_inventory_service.infrastructure.dtos.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.Length;

public record UpdateProductDTO(
  @NullOrNotBlank(message = "O nome do produto não deve estar em branco")
  @Length(max = 300, message = "O nome do produto pode ter no máximo 300 caracteres")
  @Schema(name = "name", type = "string", example = "Mouse wireless Logitech G PRO SUPERLIGHT", nullable = true)
  String name,

  @NullOrNotBlank(message = "A descrição do produto não deve estar em branco")
  @Schema(name = "description", type = "string", example = "An updated technical and descriptive text about the product", nullable = true)
  String description,

  @NullOrNotBlank(message = "O preço unitário do produto não deve estar em branco")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 1200.00")
  @Schema(name = "unitPrice", type = "string", example = "150.00", nullable = true)
  String unitPrice,

  @Nullable
  @PositiveOrZero(message = "A quantidade de unidades do produto não deve ser um número negativo")
  @Schema(name = "quantity", type = "integer", format = "int64", example = "50", nullable = true)
  Long quantity
) implements UpdateProductDomainDTO {}
