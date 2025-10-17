package com.felipe.ecommerce_discount_service.infrastructure.dtos.promotion;

import com.felipe.ecommerce_discount_service.core.application.dtos.promotion.CreatePromotionDTO;
import com.felipe.ecommerce_discount_service.infrastructure.exceptions.CreatePromotionDTOValidationException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public record CreatePromotionDTOImpl(
  @NotBlank(message = "O nome da promoção é obrigatório")
  @Length(max = 150, message = "O nome da promoção deve ter no máximo 150 caracteres")
  @Schema(name = "name", type = "string", example = "50% OFF")
  String name,

  @Nullable
  @Schema(name = "description", type = "string", nullable = true, example = "50% off discount")
  String description,

  @NotBlank(message = "O escopo da promoção é obrigatório")
  @Pattern(regexp = "^(all|specific)$", message = "Escopo inválido! Os valores aceitos são 'all' e 'specific'")
  @Schema(name = "scope", type = "string", example = "all")
  String scope,

  @NotBlank(message = "O tipo do desconto é obrigatório")
  @Pattern(regexp = "^(fixed_amount|percentage)$", message = "Tipo de desconto inválido! Os valores aceitos são 'fixed_amount' e 'percentage'")
  @Schema(name = "discountType", type = "string", example = "percentage")
  String discountType,

  @NotBlank(message = "O valor do desconto é obrigatório")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 80.00")
  @Schema(name = "discountValue", type = "string", example = "50.00")
  String discountValue,

  @NotBlank(message = "O preço mínimo da promoção é obrigatório")
  @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Formato inválido! Digite no formato válido. Ex: 1200.00")
  @Schema(name = "minimumPrice", type = "string", example = "120.00")
  String minimumPrice,

  @Valid
  @NotNull(message = "A data de término da promoção é obrigatória")
  @Schema(name = "endDate", type = "object")
  EndDateDTOImpl endDate,

  @Valid
  @NotNull(message = "Os alvos da promoção são obrigatórios")
  @Size(min = 1, message = "A promoção deve ter pelo menos 1 alvo para ser aplicada")
  @Schema(name = "promotionApplies", type = "array")
  List<PromotionAppliesToDTOImpl> promotionApplies
) implements CreatePromotionDTO {
  public CreatePromotionDTOImpl {
    if(scope.equals("specific")) {
      if(promotionApplies.size() > 3) {
        throw new CreatePromotionDTOValidationException(
          "promotionApplies",
          "Size: " + promotionApplies.size(),
          "A quantidade máxima de alvos da promoção para o escopo 'specific' são 3"
        );
      }
      if(hasDuplicate(promotionApplies)) {
        throw new CreatePromotionDTOValidationException(
          "promotionApplies",
          "Duplicated element",
          "Os alvos do promoção para o escopo 'specific' não deve conter elementos duplicados. " +
          "São aceitos a penas 1 categoria, 1 marca e 1 modelo"
        );
      }
    }
  }

  private static boolean hasDuplicate(List<PromotionAppliesToDTOImpl> promotionApplies) {
    final Set<PromotionAppliesToDTOImpl> noDuplicates = new HashSet<>(promotionApplies);
    return noDuplicates.size() != promotionApplies.size();
  }
}
